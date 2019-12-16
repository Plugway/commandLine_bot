import java.util.concurrent.ConcurrentLinkedQueue;

public class Duels {

    private static ConcurrentLinkedQueue<User> duelQueue = new ConcurrentLinkedQueue<>();

    public static void enterDuel(User user, IO botIO) throws InterruptedException, SerializationException, QuizCreationException {
        botIO.println("Введите число вопросов для дуэли.", user.getChatId());
        user.setCurrentQuestCount(QuizLogic.getTotalQuestionsToAsk(user, Question.questionsList.size(), botIO));
        if (duelQueue.size() != 0)
        {
            new QuizLogic(botIO, user, duelQueue.poll()).runQuiz();
        }
        else
        {
            duelQueue.add(user);
            botIO.println("Ждем противника.", user.getChatId());
            duelWaiting(user);
        }
    }

    private static void duelWaiting(User user) throws InterruptedException {
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