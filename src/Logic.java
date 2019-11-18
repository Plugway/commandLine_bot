import java.io.*;
import java.util.function.Consumer;

public class Logic {
    public Logic(User user) {
        this.user = user;
        this.chatId = user.getChatId();
        this.botIO = Main.botIO;
    }

    private static String helpText = "Привет, я бот, вот что я умею:\n" +
            "1. Задавать вопросики, на которые тебе нужно ответить.\n\n" +
            "Чтобы начать, напиши /start\n" +
            "Для вызова помощи напиши /help\n" +
            "Чтобы выйти во время викторины напиши /exit";

    private static long adminChatId = 0;
    private static long adminWantId = 0;

    public static void setAdminId(long adminChat, long adminWant) {
        adminChatId = adminChat;
        adminWantId = adminWant;
    }

    public static String getHelpText() {
        return helpText;
    }

    private User user;
    private long chatId;
    private IO botIO;

    public static void initializeAllUserThreads() {
        for (User user : UserTable.get()) {
            System.out.println(user.getChatId());
            UserInteractionThreads.createThread(user, false);
        }
    }

    public void startUserInteraction() throws IOException, InterruptedException, SerializationException {
        botIO.println(helpText, chatId);
        resumeUserInteraction();
    }

    public void resumeUserInteraction() throws IOException, InterruptedException, SerializationException {
        while (true) {
            var userInput = botIO.readUserQuery(user);
            if (adminChatId != 0 && adminWantId == chatId)
                botIO.println(userInput, adminChatId);
            else
                UserCommandHandler.preQuizResolveCommand(userInput, user);
            //UserCommandHandler.preQuizResolveCommand(botIO.readUserQuery(user), user);
        }
    }
}