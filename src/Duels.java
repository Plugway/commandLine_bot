import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Duels extends QuizLogic {
    public Duels(User user1, User user2, IO botIO)
    {
        this.user1 = user1;
        this.user2 = user2;
        user1Id = user1.getChatId();
        user2Id = user2.getChatId();
        this.questNum = (user1.getDuelQuestCount()+user2.getDuelQuestCount())/2;
        this.botIO = botIO;
        user1.setDuelId(user2.getChatId());
        user2.setDuelId(user1.getChatId());
    }

    public static Queue<User> duelQueue = new LinkedList<>();
    User user1;
    User user2;
    long user1Id;
    long user2Id;
    int questNum;
    IO botIO;

    public static void enterDuel(User user, IO botIO) throws InterruptedException {
        botIO.println("Введите число вопросов для дуэли.", user.getChatId());
        user.setDuelQuestCount(QuizLogic.getTotalQuestionsToAsk(user, questionsList.size(), botIO));
        if (duelQueue.size() != 0)
        {
            new Duels(user, duelQueue.poll(), botIO).runDuel();
        }
        else
        {
            duelQueue.add(user);
            botIO.println("Ждем противника.", user.getChatId());
            duelWaiting(user);
        }
    }
    public void runDuel()
    {
        var questions = new ArrayList<>(questionsList);
        Collections.shuffle(questions);
        var currentQuestionNumber = 0;
        var questionsAskedQuantity = 0;
        var score1 = 0;
        var score2 = 0;
        try {
            botIO.println("Число вопросов, предложенное первым игроком - " + user1.getDuelQuestCount() +
                    ", вторым - " + user2.getDuelQuestCount() + ". Число вопросов в дуэли: " + questNum, user1Id, user2Id);
            for (currentQuestionNumber = 0; currentQuestionNumber < questNum; ++currentQuestionNumber) {
                var currentQuestion = questions.get(currentQuestionNumber);

                botIO.println((currentQuestionNumber + 1) + styleDelimiter + currentQuestion.getQuestionText(),
                        user1Id,
                        user2Id);         //печатаем вопрос
                for (var i = 0; i < currentQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                {
                    botIO.println(currentQuestion.getAnswers().get(i), user1Id, user2Id);
                }

                var intInput = handleUsersDuelInput(user1, user2, botIO);
                if (Question.isAnswersRight(intInput[0], currentQuestion.getRightAnswers())) {
                    botIO.println("Верно!", user1Id);
                    score1++;
                } else
                    botIO.println("Неверно :(", user1Id);
                if (Question.isAnswersRight(intInput[1], currentQuestion.getRightAnswers())) {
                    botIO.println("Верно!", user2Id);
                    score2++;
                } else
                    botIO.println("Неверно :(", user2Id);

                questionsAskedQuantity++;
                if (currentQuestionNumber == questNum - 1)
                    throw new DuelShouldFinishException();
            }
        } catch (DuelShouldFinishException | InterruptedException e) {
            botIO.println("Дуэль завершена.", user1Id, user2Id);
            botIO.println("Твой счет: " + score1 + " из " + questionsAskedQuantity, user1Id);
            botIO.println("Твой счет: " + score2 + " из " + questionsAskedQuantity, user2Id);
            if (score1 < score2)
                printDuelResult(user1, user2, score1, score2);
            else if (score1 > score2)
                printDuelResult(user2, user1, score2, score1);
            else
                botIO.println("Ничья :|", user1Id, user2Id);
        } catch (DuelInterruptedException e)
        {
            botIO.println("Дуэль прервана.", user1Id, user2Id);
            var cause = e.getMessage().split(",");
            if (cause[1].equals("1"))
                printDuelInterruptedMessages(user1, user2, score1, score2, cause[0]);
            else
                printDuelInterruptedMessages(user2, user1, score2, score1, cause[0]);
        }
        user1.setDuelId(0);
        user2.setDuelId(0);
        user1.setDuelQuestCount(0);
        user2.setDuelQuestCount(0);
    }
    private void printDuelInterruptedMessages(User user1, User user2, int score1, int score2, String cause)
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
                botIO.println("По какой-то причине твой потивник ушел... Можешь считать что тебе повезло ведь он лидировал.\nСейчас ты вернешься в главное меню.", user2.getChatId());
                botIO.println("Ты выходишь в главное меню.", user1.getChatId());
            }
            else
            {
                botIO.println("По какой-то причине твой потивник ушел...\nСейчас ты вернешься в главное меню.", user2.getChatId());
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
    private int[][] handleUsersDuelInput(User user1, User user2, IO botIO) throws InterruptedException, DuelShouldFinishException, DuelInterruptedException {
        int[][] intInput = new int[2][];
        while (true) {
            var input = botIO.readUsersQueries(user1, user2);
            if (input[0].equals("/exit")) {
                throw new DuelInterruptedException("desire,1");
            }
            if (input[1].equals("/exit"))
            {
                throw new DuelInterruptedException("desire,2");
            }
            intInput[0] = getIntInpArray(input[0]);
            intInput[1] = getIntInpArray(input[1]);
            return intInput;
        }
    }
    private int[] getIntInpArray(String input)
    {
        try {
            return getIntInputArray(input);
        } catch (Exception e) {
            return new int[1];
        }
    }
    private void printDuelResult(User user1, User user2, int score1, int score2)
    {
        botIO.println("Ты проиграл! Твой счет: "+score1+", счет противника: "+score2, user1.getChatId());
        botIO.println("Ты выиграл! Твой счет: "+score2+", счет противника: "+score1, user2.getChatId());
    }
    public static void duelWaiting(User user) throws InterruptedException {
        while (duelQueue.size() != 0)
        {
            Thread.sleep(1000);
        }
        duelProcessing(user);
    }
    private static void duelProcessing(User user) throws InterruptedException {
        while (user.getDuelId() != 0)
        {
            Thread.sleep(1000);
        }
    }
}
