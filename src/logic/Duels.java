import java.util.concurrent.ConcurrentLinkedQueue;

public class Duels {

    private static ConcurrentLinkedQueue<User> duelQueue = new ConcurrentLinkedQueue<>();

    public static void enterDuel(User user, IO botIO) throws InterruptedException, QuizCreationException {
        botIO.println("Введите число вопросов для дуэли.", user.getChatId());
        user.setCurrentQuestCount(QuizLogic.getTotalQuestionsToAsk(user, Question.getQuestionsList().size(), botIO));
        if (duelQueue.size() != 0)
        {
            new QuizLogic(botIO, user, duelQueue.poll()).runQuiz();
        }
        else
        {
            duelQueue.add(user);
            botIO.println("Ждем противника.", user.getChatId());
            try {
                duelWaiting(user, botIO);
            } catch (ExitingLobbyException e) {
                duelQueue.remove(user);
                botIO.println("Вы вышли из лобби.", user.getChatId());
            }
        }
    }

    private static void duelWaiting(User user, IO botIO) throws InterruptedException, ExitingLobbyException {
        var messages = user.messages;
        while (duelQueue.size() != 0)
        {
            if (messages.size() != 0)
                UserCommandHandler.preDuelResolveCommand(botIO.readUserQuery(user), user);
            Thread.sleep(1000);
        }
        duelProcessing(user);
    }

    private static void duelProcessing(User user) throws InterruptedException {
        Thread.sleep(1000); //чтобы успеть переключиться на дуэль
        while (user.playsDuel())
        {
            Thread.sleep(1000);
        }
    }
}