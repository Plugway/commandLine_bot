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

    private static Queue<User> duelQueue = new LinkedList<>();
    private User user1;
    private User user2;
    private long user1Id;
    private long user2Id;
    private int questNum;
    private IO botIO;

    public static void enterDuel(User user, IO botIO) throws InterruptedException {
        botIO.println("Введите число вопросов для дуэли.", user.getChatId());
        user.setDuelQuestCount(QuizLogic.getTotalQuestionsToAsk(user, Question.questionsList.size(), botIO));
        if (duelQueue.size() != 0)
        {
            new Duels(user, duelQueue.poll(), botIO).runDuel();
        }
        else
        {
            duelQueue.add(user);
            botIO.println("Ждем противника.", user.getChatId());
            duelWaiting(user, botIO);
        }
    }

    private void runDuel()
    {
        var questions = new ArrayList<>(Question.questionsList);
        Collections.shuffle(questions);
        var currentQuestionNumber = 0;
        var questionsAskedQuantity = 0;
        var score1 = 0;
        var score2 = 0;
        try {
            botIO.println("Число вопросов, предложенное первым игроком - " + user1.getDuelQuestCount() +
                    ", вторым - " + user2.getDuelQuestCount() + ". Число вопросов в дуэли: " + questNum, user1Id, user2Id);
            for (currentQuestionNumber = 0; currentQuestionNumber < questNum; ++currentQuestionNumber)
            {
                var currentQuestion = questions.get(currentQuestionNumber);

                botIO.println((currentQuestionNumber + 1) + styleDelimiter + currentQuestion.getQuestionText(),
                        user1Id,
                        user2Id);         //печатаем вопрос
                if (currentQuestion instanceof NumQuestion)
                {
                    var currentNumQuestion = (NumQuestion)currentQuestion;
                    for (var i = 0; i < currentNumQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                    {
                        botIO.println(currentNumQuestion.getAnswers().get(i), user1Id, user2Id);
                    }
                    var intInput = handleIntUsersDuelInput(user1, user2, botIO);
                    score1 += getScoreAdd(currentNumQuestion.assertAnswers(intInput[0]), user1);
                    score2 += getScoreAdd(currentNumQuestion.assertAnswers(intInput[1]), user2);
                }
                else if (currentQuestion instanceof WordQuestion)
                {
                    var currentWordQuestion = (WordQuestion)currentQuestion;
                    var input = handleStrUsersDuelInput(user1, user2, botIO);
                    score1 += getScoreAdd(currentWordQuestion.assertAnswer(input[0]), user1);
                    score2 += getScoreAdd(currentWordQuestion.assertAnswer(input[1]), user2);
                }
                questionsAskedQuantity++;
                if (currentQuestionNumber == questNum - 1)
                    throw new DuelShouldFinishException();
            }
        } catch (DuelShouldFinishException | InterruptedException e) {
            printResultsDuelFinished(score1, score2, questionsAskedQuantity);
        } catch (DuelInterruptedException e) {
            user1.getStats().addDuelCount(user1, botIO);
            user2.getStats().addDuelCount(user2, botIO);
            printResultsDuelInterrupted(score1, score2, e);
        }
        user1.setDuelId(0);
        user2.setDuelId(0);
        user1.setDuelQuestCount(0);
        user2.setDuelQuestCount(0);
    }

    private int getScoreAdd(boolean ansRight, User user)
    {
        if (ansRight) {
            botIO.println("Верно!", user.getChatId());
            return 1;
        } else
        {
            botIO.println("Неверно :(", user.getChatId());
            return 0;
        }
    }

    private void printResultsDuelFinished(int score1, int score2, int questionsAskedQuantity)
    {
        botIO.println("Дуэль завершена.", user1Id, user2Id);
        botIO.println("Твой счет: " + score1 + " из " + questionsAskedQuantity, user1Id);
        botIO.println("Твой счет: " + score2 + " из " + questionsAskedQuantity, user2Id);
        if (score1 < score2)
            printDuelResult(user1, user2, score1, score2);
        else if (score1 > score2)
            printDuelResult(user2, user1, score2, score1);
        else
            botIO.println("Ничья :|", user1Id, user2Id);
    }

    private void printResultsDuelInterrupted(int score1, int score2, DuelInterruptedException e)
    {
        botIO.println("Дуэль прервана.", user1Id, user2Id);
        var cause = e.getMessage().split(",");
        if (cause[1].equals("1"))
            printDuelInterruptedMessages(user1, user2, score1, score2, cause[0]);
        else
            printDuelInterruptedMessages(user2, user1, score2, score1, cause[0]);
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

    private int[][] handleIntUsersDuelInput(User user1, User user2, IO botIO) throws InterruptedException, DuelInterruptedException {
        int[][] intInput = new int[2][];
        while (true) {
            var input = botIO.readDuelUsersQueries(user1, user2);
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

    private String[] handleStrUsersDuelInput(User user1, User user2, IO botIO) throws InterruptedException, DuelInterruptedException {
        while (true) {
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
        user1.getStats().addDuelLostCount(user1, botIO);
        botIO.println("Ты проиграл! Твой счет: "+score1+", счет противника: "+score2, user1.getChatId());
        user2.getStats().addDuelWinsCount(user2, botIO);
        botIO.println("Ты выиграл! Твой счет: "+score2+", счет противника: "+score1, user2.getChatId());
    }

    private static void duelWaiting(User user, IO botIO) throws InterruptedException {
        //try {
            while (duelQueue.size() != 0)
            {
                Thread.sleep(1000);
                //UserCommandHandler.preDuelResolveCommand(botIO.readUserQuery(user), user);
            }
            duelProcessing(user, botIO);
        //} catch (ExitingLobbyException e)
        //{
            /*
            broken. user never actually leaves the queue
            botIO.println("Вы вышли из лобби.", user.getChatId());
            user.setDuelId(0);
            user.setDuelQuestCount(0);
             */
        //}
    }

    private static void duelProcessing(User user, IO botIO) throws InterruptedException {
        while (user.getDuelId() != 0)   //uuuh, maybe just make this a condition in duelWaiting's while loop? why a separate method?
        {
            Thread.sleep(1000);
            //UserCommandHandler.preDuelResolveCommand(botIO.readUserQuery(user), user);
        }
    }
}