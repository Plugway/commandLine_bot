import java.util.*;

public class QuizLogic {

    public QuizLogic(IO botIO, User... users) throws QuizCreationException {
        verifyUsersLength(users);
        this.botIO = botIO;
        initializeUsers(users);
    }

    private static final String styleDelimiter = ")";  //Скобочка, идущая после номера вопроса

    private static Random rnd = new Random();
    private static int getRandom(int min, int max) {
        return min + rnd.nextInt(max - min + 1);
    } //useless shit

    private void verifyUsersLength(User... users) throws QuizCreationException {
        if (users.length > 2 || users.length == 0) {
            Logger.log(LogLevels.error, "QuizLogic: Can't create quiz with "+users.length+" users.");
            throw new QuizCreationException("Unable to create a quiz with more than 2 users.");
        }
    }

    public static void enterQuiz(User user, IO botIO) throws InterruptedException, QuizCreationException {
        botIO.println("Сколько вопросов?", user.getChatId());
        user.setCurrentQuestCount(getTotalQuestionsToAsk(user, Question.getQuestionsList().size(), botIO));
        new QuizLogic(botIO, user).runQuiz();
    }

    private User[] users;
    private long[] usersIds;
    private int[] usersScores;
    private int questionsAskedQuantity = 0;
    private int totalQuestionsToAsk;
    private IO botIO;

    public void runQuiz() throws InterruptedException{

        determineTotalQuestions();

        var questions = new ArrayList<>(Question.getQuestionsList());
        Collections.shuffle(questions, rnd);

        try {
            if (users.length > 1)
                botIO.println(getStringToNotifyQuestCount(), usersIds);
            int currentQuestionNumber;
            for (currentQuestionNumber = 0; currentQuestionNumber < totalQuestionsToAsk; ++currentQuestionNumber) {
                var currentQuestion = questions.get(currentQuestionNumber);

                botIO.println((currentQuestionNumber + 1) + styleDelimiter + currentQuestion.getQuestionText(),
                        usersIds);         //печатаем вопрос
                if (currentQuestion instanceof NumQuestion)
                    handleNumQuestion(currentQuestion);
                else if (currentQuestion instanceof WordQuestion)
                    handleWordQuestion(currentQuestion);
                questionsAskedQuantity++;
                for (User user : users)
                    user.getStats().addQuestionsCount(user, botIO);
                if (currentQuestionNumber == totalQuestionsToAsk - 1) {
                    if (users.length == 1)
                        throw new QuizShouldFinishException();
                    else
                        throw new DuelShouldFinishException();
                }
            }
        } catch (QuizShouldFinishException e) {
            handleResultsQuizFinished();
        } catch (DuelShouldFinishException e) {
            for (User user : users)
                user.getStats().addDuelCount(user, botIO);
            printResultsDuelFinished();
        } catch (DuelInterruptedException e) {
            printResultsDuelInterrupted(e);
        }
        resetUserValues();
    }

    private void handleNumQuestion(Question currentQuestion) throws QuizShouldFinishException, InterruptedException, DuelInterruptedException {
        var currentNumQuestion = (NumQuestion)currentQuestion;
        var answers = currentNumQuestion.getAnswers();
        for (String answer : answers) botIO.println(answer, usersIds);  //печатаем ответы
        if (users.length == 1) {
            var intInput = handleIntUserQuizInput(users[0], botIO);
            usersScores[0] += getScoreAdd(currentNumQuestion.assertAnswers(intInput), users[0], botIO);
        }
        else {
            var intInput = handleIntUsersDuelInput(users[0], users[1], botIO);
            for (var i = 0; i < users.length; i++)
                usersScores[i] += getScoreAdd(currentNumQuestion.assertAnswers(intInput[i]), users[i], botIO);
        }
    }

    private void handleWordQuestion(Question currentQuestion) throws QuizShouldFinishException, InterruptedException, DuelInterruptedException {
        var currentWordQuestion = (WordQuestion)currentQuestion;
        if (users.length == 1) {
            usersScores[0] += getScoreAdd(currentWordQuestion.assertAnswer(handleStrUserQuizInput(users[0], botIO)), users[0], botIO);
        }
        else {
            var input = handleStrUsersDuelInput(users[0], users[1], botIO);
            for (var i = 0; i < users.length; i++)
                usersScores[i] += getScoreAdd(currentWordQuestion.assertAnswer(input[i]), users[i], botIO);
        }
    }

    private String getStringToNotifyQuestCount()
    {
        var builder = new StringBuilder().append("Количество вопросов, предложенное игроками:\n");
        for (var i = 0; i < users.length;i++)
        {
            builder.append(i).append(" - ").append(users[i].getCurrentQuestCount()).append("\n");
        }
        builder.append("Число вопросов в дуэли: ").append(totalQuestionsToAsk);
        return builder.toString();
    }

    private void determineTotalQuestions() {
        var questionsSum = 0;
        for (User user : users)
            questionsSum+=user.getCurrentQuestCount();
        totalQuestionsToAsk = questionsSum/users.length;
    }

    private void printResultsDuelInterrupted(DuelInterruptedException e)
    {
        botIO.println("Дуэль прервана.", usersIds);
        if (e.getUserNumber() == 1)
            printDuelInterruptedMessages(users[0], users[1], usersScores[0], usersScores[1], e.getDuelCause(), botIO);
        else
            printDuelInterruptedMessages(users[1], users[0], usersScores[1], usersScores[0], e.getDuelCause(), botIO);
    }
    private void printDuelInterruptedMessages(User user1, User user2, int score1, int score2, DuelInterruptedCause cause, IO botIO)
    {
        if (cause == DuelInterruptedCause.desire)
        {
            if (score1 < score2)
            {
                botIO.println("Твой противник сбежал! Ну это и понятно, ведь дела у него были так себе) Можешь считать себя победителем.\uD83C\uDFC5\nА сейчас ты переместишься в главное меню.", user2.getChatId());
                botIO.println("Ты, поджав хвост, сбегаешь в главное меню.", user1.getChatId());
            }
            else if (score1 > score2)
            {
                botIO.println("По какой-то причине твой противник ушел... Можешь считать что тебе повезло ведь он лидировал.\nСейчас ты вернешься в главное меню.", user2.getChatId());
                botIO.println("Ты выходишь в главное меню.", user1.getChatId());
            }
            else
            {
                botIO.println("По какой-то причине твой противник ушел...\nСейчас ты вернешься в главное меню.", user2.getChatId());
                botIO.println("Ты выходишь в главное меню.", user1.getChatId());
            }
        }
        else
        {
            if (score1 < score2)
            {
                botIO.println("Ты так долго думал, что твой противник не выдержал и сбежал, поджав хвост. Можешь считать себя полноправным победителем!\uD83C\uDFC5\nА сейчас ты переместишься в главное меню.", user2.getChatId());
                botIO.println("Ты, поджав хвост, сбегаешь в главное меню.", user1.getChatId());
            }
            else if (score1 > score2){
                botIO.println("Ты так долго думал, что твой противник не посчитал нужным ждать тебя. Считай, что тебе повезло, ведь дела у тебя были не очень.\nСейчас ты вернешься в главное меню.", user2.getChatId());
                botIO.println("Ты выходишь в главное меню.", user1.getChatId());
            }
            else
            {
                botIO.println("Ты так долго думал, что твой противник не посчитал нужным ждать тебя.\nСейчас ты вернешься в главное меню.", user2.getChatId());
                botIO.println("Ты выходишь в главное меню.", user1.getChatId());
            }
        }
    }
    private void printResultsDuelFinished()
    {
        botIO.println("Дуэль завершена.", usersIds);
        if (usersScores[0] < usersScores[1])
            printDuelResult(users[0], users[1], usersScores[0], usersScores[1], botIO);
        else if (usersScores[0] > usersScores[1])
            printDuelResult(users[1], users[0], usersScores[1], usersScores[0], botIO);
        else {
            botIO.println("Твой счет: " + usersScores[0] + " из " + questionsAskedQuantity, usersIds);
            botIO.println("Ничья :|", usersIds);
        }
    }

    private void printDuelResult(User user1, User user2, int score1, int score2, IO botIO)
    {
        user1.getStats().addDuelLostCount(user1, botIO);
        botIO.println("Ты проиграл! Твой счет: "+score1+", счет противника: "+score2, user1.getChatId());
        user2.getStats().addDuelWinsCount(user2, botIO);
        botIO.println("Ты выиграл! Твой счет: "+score2+", счет противника: "+score1, user2.getChatId());
    }

    private void handleResultsQuizFinished() {
        botIO.println("Викторина завершена.", usersIds[0]);
        botIO.println("Твой счет: " + usersScores[0] + " из " + questionsAskedQuantity, usersIds[0]);
        if (users[0].getHighscore() < usersScores[0]) {
            botIO.println("Новый рекорд\uD83C\uDF89", usersIds[0]);
            users[0].getStats().addQuizHighscoreHitCount(users[0], botIO);
            users[0].getStats().setHighscore(usersScores[0], users[0], botIO);
        }
    }

    private void initializeUsers(User... users)
    {
        this.users = users;
        usersIds = Arrays.stream(users).mapToLong(User::getChatId).toArray();//peek(User::togglePlaysDuel) включает режим дуэли
        for (User user:users)
            user.togglePlaysDuel();
        usersScores = new int[users.length];
    }

    private int getScoreAdd(boolean ansRight, User user, IO botIO)
    {
        if (ansRight) {
            botIO.println("Верно!", user.getChatId());
            user.getStats().addRightQuestionsCount(user, botIO);
            return 1;
        } else
        {
            botIO.println("Неверно :(", user.getChatId());
            return 0;
        }
    }
    private String handleStrUserQuizInput(User user, IO botIO) throws InterruptedException, QuizShouldFinishException {
        while (true) {
            var input = botIO.readUserQuery(user);
            if (input.substring(0, 1).equals("/")) {
                UserCommandHandler.quizResolveCommand(input, user);
                continue;
            }
            return input;
        }
    }
    private int[] handleIntUserQuizInput(User user, IO botIO) throws InterruptedException, QuizShouldFinishException {
        int[] intInput;
        while (true) {
            var input = botIO.readUserQuery(user);
            if (input.substring(0, 1).equals("/")) {
                UserCommandHandler.quizResolveCommand(input, user);
                continue;
            }
            try {
                intInput = getIntInputArray(input);
                return intInput;
            } catch (Exception e) {
                botIO.println("Введите числа, соответствующие ответам.\nЕсли вы считаете, что ответов несколько, то введите их через запятую.", user.getChatId());
            }
        }
    }

    public static int getTotalQuestionsToAsk(User user, int totalQuestionsAvailable, IO botIO) {
        botIO.println("Общее число вопросов: " + totalQuestionsAvailable, user.getChatId());
        int totalQuestionsToAsk = 0;
        var inputIsGood = false;
        var failNum = new String[]{
                "Возможно, тебе не стоит учавствовать в этом, если у тебя этот вопрос вызывает затруднения...",
                "Число вопросов должно быть быть меньше " + totalQuestionsAvailable + " и больше 0."
        };
        while (!inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(botIO.readUserQuery(user));
                if (totalQuestionsToAsk <= totalQuestionsAvailable && totalQuestionsToAsk > 0) {
                    inputIsGood = true;
                } else
                    botIO.println(failNum[getRandom(0, failNum.length - 1)], user.getChatId());
            } catch (Exception e) {
                botIO.println("Введите натуральное число, которое больше 0 и меньше либо равно " + totalQuestionsAvailable + ".", user.getChatId());
            }
        }
        return totalQuestionsToAsk;
    }

    private static int[] getIntInputArray(String input) throws Exception {
        var numinput = input.replaceAll(" ", "").split(",");
        var intInput = new int[numinput.length];
        for (String s : numinput)
            if (Integer.parseInt(s) <= 0)
                throw new Exception();
        for (var j = 0; j < numinput.length; j++)
            intInput[j] = Integer.parseInt(numinput[j]);
        return intInput;
    }

    private int[][] handleIntUsersDuelInput(User user1, User user2, IO botIO) throws InterruptedException, DuelInterruptedException {
        int[][] intInput = new int[2][];
        var input = botIO.readDuelUsersQueries(user1, user2);
        if (input[0].equals("/exit")) {
            throw new DuelInterruptedException(1, DuelInterruptedCause.desire);
        }
        if (input[1].equals("/exit")) {
            throw new DuelInterruptedException(2, DuelInterruptedCause.desire);
        }
        intInput[0] = getIntInpArray(input[0]);
        intInput[1] = getIntInpArray(input[1]);
        return intInput;
    }
    private int[] getIntInpArray(String input)
    {
        try {
            return getIntInputArray(input);         //Ответы не переспрашиваются при неверном вводе
        } catch (Exception e) {
            return new int[1];
        }
    }
    private String[] handleStrUsersDuelInput(User user1, User user2, IO botIO) throws InterruptedException, DuelInterruptedException {
        var input = botIO.readDuelUsersQueries(user1, user2);
        if (input[0].equals("/exit")) {
            throw new DuelInterruptedException(1, DuelInterruptedCause.desire);
        }
        if (input[1].equals("/exit"))
        {
            throw new DuelInterruptedException(2, DuelInterruptedCause.desire);
        }
        return input;
    }

    private void resetUserValues()
    {
        for (User user:users)
        {
            user.togglePlaysDuel();
            user.setCurrentQuestCount(0);
        }
    }
}