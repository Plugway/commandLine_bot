import java.io.*;

public class Logic {
    public Logic(User user) {
        this.user = user;
        chatId = user.getChatId();
    }

    private static String helpText = "Привет, я бот, вот что я умею:\n" +
            "1. Задавать вопросики, на которые тебе нужно ответить.\n\n" +
            "Чтобы начать, напиши /start\n" +
            "Для вызова помощи напиши /help\n" +
            "Чтобы выйти во время викторины напиши /exit";

    public static String getHelpText() {
        return helpText;
    }

    private User user;
    private long chatId;

    public static void initializeAllUserThreads(IO botIO) {
        for (User user : UserTable.get())
            UserInteractionThreads.createThread(user, false, botIO);
    }

    public void startUserInteraction(IO botIO) throws IOException, InterruptedException {
        botIO.println(helpText, chatId);
        while (true) {
            try {
                UserCommandHandler.resolveCommand(botIO.readUserQuery(user), user, false, botIO);   //that "false" that gets passed feels like a kludge
            } catch (QuizShouldFinishException ignored) {}      // only handled in QuizLogic
        }
    }

    public void resumeUserInteraction(IO botIO) throws IOException, InterruptedException {
        while (true) {
            try {
                UserCommandHandler.resolveCommand(botIO.readUserQuery(user), user, false, botIO);   //that "false" that gets passed feels like a kludge
            } catch (QuizShouldFinishException ignored) {}      // only handled in QuizLogic
        }
    }
}