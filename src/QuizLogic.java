import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizLogic {

    public QuizLogic(IO botIO, User... users) throws QuizCreationException {
        verifyUsersLength(users);
        this.botIO = botIO;
        if (users.length == 2)
            initializeDuelingUsers(users);
        else
            initializeQuizUser(users[0]);
    }

    private static final String styleDelimiter = ")";

    private static Random rnd = new Random(System.nanoTime());
    private static int getRandom(int min, int max) {
        return min + rnd.nextInt(max - min + 1);
    }

    public void verifyUsersLength(User... users) throws QuizCreationException {
        if (users.length > 2)
            throw new QuizCreationException("Unable to create a quiz with more than 2 users.");
        else if (users.length == 0)
            throw new QuizCreationException("Unable to create a quiz with 0 users.");
    }

    public static void enterQuiz(User user, IO botIO) throws SerializationException, InterruptedException, QuizCreationException {
        botIO.println("Сколько вопросов?", user.getChatId());
        user.setCurrentQuestCount(getTotalQuestionsToAsk(user, Question.questionsList.size(), botIO));
        new QuizLogic(botIO, user).runQuiz();
    }

    private User user1;
    private User user2;
    private int questionsAskedQuantity = 0;
    private int score1 = 0;
    private int score2 = 0;
    private long user1Id;
    private long user2Id;
    private int totalQuestionsToAsk;
    private IO botIO;

    public void runQuiz() throws InterruptedException, SerializationException {

        determineTotalQuestionsAndIDs();

        var questions = new ArrayList<>(Question.questionsList);
        Collections.shuffle(questions, rnd);

        try {
            if (user2 != null)
                botIO.println("Число вопросов, предложенное первым игроком - " + user1.getCurrentQuestCount() +
                        ", вторым - " + user2.getCurrentQuestCount() + ". Число вопросов в дуэли: " + totalQuestionsToAsk, user1Id, user2Id);
            int currentQuestionNumber;
            for (currentQuestionNumber = 0; currentQuestionNumber < totalQuestionsToAsk; ++currentQuestionNumber) {
                var currentQuestion = questions.get(currentQuestionNumber);

                botIO.println((currentQuestionNumber + 1) + styleDelimiter + currentQuestion.getQuestionText(), user1Id, user2Id);         //печатаем вопрос
                if (currentQuestion instanceof NumQuestion)
                    handleNumQuestion(currentQuestion);
                else if (currentQuestion instanceof WordQuestion)
                    handleWordQuestion(currentQuestion);
                questionsAskedQuantity++;
                user1.getStats().addQuestionsCount(user1, botIO);
                if (user2 != null)
                    user2.getStats().addQuestionsCount(user2,botIO);
                if (currentQuestionNumber == totalQuestionsToAsk - 1) {
                    if (user2 == null)
                        throw new QuizShouldFinishException();
                    else
                        throw new DuelShouldFinishException();
                }
            }
        } catch (QuizShouldFinishException e) {
            handleResultsQuizFinished();
        } catch (DuelShouldFinishException e) {
            user1.getStats().addDuelCount(user1, botIO);
            user2.getStats().addDuelCount(user2, botIO);
            printResultsDuelFinished();
        } catch (DuelInterruptedException e) {
            printResultsDuelInterrupted(e);
        }
        resetUserValues();
    }

    private void handleNumQuestion(Question currentQuestion) throws QuizShouldFinishException, InterruptedException, DuelInterruptedException {
        var currentNumQuestion = (NumQuestion)currentQuestion;
        for (var i = 0; i < (currentNumQuestion).getAnswers().size(); i++)                                       //печатаем ответы
            botIO.println((currentNumQuestion).getAnswers().get(i),  user1Id, user2Id);
        if (user2 == null) {
            var intInput = handleIntUserQuizInput(user1, botIO);
            score1 += getScoreAdd(currentNumQuestion.assertAnswers(intInput), user1, botIO);
        }
        else {
            var intInput = handleIntUsersDuelInput(user1, user2, botIO);
            score1 += getScoreAdd(currentNumQuestion.assertAnswers(intInput[0]), user1, botIO);
            score2 += getScoreAdd(currentNumQuestion.assertAnswers(intInput[1]), user2, botIO);
        }
    }

    private void handleWordQuestion(Question currentQuestion) throws QuizShouldFinishException, InterruptedException, DuelInterruptedException {
        var currentWordQuestion = (WordQuestion)currentQuestion;
        if (user2 == null) {
            score1 += getScoreAdd(currentWordQuestion.assertAnswer(handleStrUserQuizInput(user1, botIO)), user1, botIO);
        }
        else {
            var input = handleStrUsersDuelInput(user1, user2, botIO);
            score1 += getScoreAdd(currentWordQuestion.assertAnswer(input[0]), user1, botIO);
            score2 += getScoreAdd(currentWordQuestion.assertAnswer(input[1]), user2, botIO);
        }
    }

    private void determineTotalQuestionsAndIDs() {
        user1Id = user1.getChatId();
        user2Id = 0;
        totalQuestionsToAsk = 0;
        if (user2 == null)
            totalQuestionsToAsk = user1.getCurrentQuestCount();
        else {
            totalQuestionsToAsk = (user1.getCurrentQuestCount() + user2.getCurrentQuestCount()) / 2;
            user2Id = user2.getChatId();
        }
    }

    private void printResultsDuelInterrupted(DuelInterruptedException e)
    {
        var user1Id = user1.getChatId();
        var user2Id = user2.getChatId();
        botIO.println("Дуэль прервана.", user1Id, user2Id);
        var cause = e.getMessage().split(",");
        if (cause[1].equals("1"))
            printDuelInterruptedMessages(user1, user2, score1, score2, cause[0], botIO);
        else
            printDuelInterruptedMessages(user2, user1, score2, score1, cause[0], botIO);
    }
    private void printDuelInterruptedMessages(User user1, User user2, int score1, int score2, String cause, IO botIO)
    {
        if (cause.equals("desire"))
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
        var user1Id = user1.getChatId();
        var user2Id = user2.getChatId();
        botIO.println("Дуэль завершена.", user1Id, user2Id);
        if (score1 < score2)
            printDuelResult(user1, user2, score1, score2, botIO);
        else if (score1 > score2)
            printDuelResult(user2, user1, score2, score1, botIO);
        else {
            botIO.println("Твой счет: " + score1 + " из " + questionsAskedQuantity, user1Id, user2Id);
            botIO.println("Ничья :|", user1Id, user2Id);
        }
    }

    private void printDuelResult(User user1, User user2, int score1, int score2, IO botIO)
    {
        user1.getStats().addDuelLostCount(user1, botIO);
        botIO.println("Ты проиграл! Твой счет: "+score1+", счет противника: "+score2, user1.getChatId());
        user2.getStats().addDuelWinsCount(user2, botIO);
        botIO.println("Ты выиграл! Твой счет: "+score2+", счет противника: "+score1, user2.getChatId());
    }

    private void handleResultsQuizFinished() throws SerializationException {
        botIO.println("Викторина завершена.", user1.getChatId());
        botIO.println("Твой счет: " + score1 + " из " + questionsAskedQuantity, user1.getChatId());
        if (user1.getHighscore() < score1) {
            botIO.println("Новый рекорд\uD83C\uDF89", user1.getChatId());
            user1.getStats().addQuizHighscoreHitCount(user1, botIO);
            user1.getStats().setHighscore(score1, user1, botIO);
        }
    }

    private void initializeDuelingUsers(User... users)
    {
        user1 = users[0];
        user2 = users[1];
        user1Id = user1.getChatId();
        user2Id = user2.getChatId();
        user1.setDuelId(user2.getChatId());
        user2.setDuelId(user1.getChatId());
    }
    private void initializeQuizUser(User user)
    {
        user1 = user;
        user1Id = user.getChatId();
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
        while (!inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(botIO.readUserQuery(user));
                var failNum = new String[]{
                        "Возможно, тебе не стоит учавствовать в этом, если у тебя этот вопрос вызывает затруднения...",
                        "Число вопросов должно быть быть меньше " + totalQuestionsAvailable + " и больше 0."
                };
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

    public static int[] getIntInputArray(String input) throws Exception {
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
            throw new DuelInterruptedException("desire,1");
        }
        if (input[1].equals("/exit")) {
            throw new DuelInterruptedException("desire,2");
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
            throw new DuelInterruptedException("desire,1");
        }
        if (input[1].equals("/exit"))
        {
            throw new DuelInterruptedException("desire,2");
        }
        return input;
    }

    private void resetUserValues()
    {
        user1.setCurrentQuestCount(0);
        if (user2 != null)
        {
            user1.setDuelId(0);
            user2.setDuelId(0);
            user2.setCurrentQuestCount(0);
        }
    }
}