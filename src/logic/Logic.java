import java.io.*;
import java.util.function.Consumer;

public class Logic {
    public Logic(User user) {
        this.user = user;
        this.chatId = user.getChatId();
        this.botIO = Main.botIO;
    }

    private static String helpText = "Привет, я бот-викторина. Я умею задавать вопросики, а ты, может быть, умеешь на них отвечать :)\n\n" +
            "Чтобы начать, напиши /start\n" +
            "Чтобы вызвать помощь, напиши /help\n" +
            "Чтобы посмотреть рекорды, напиши /top\n" +
            "Чтобы войти в лобби для игры с другим пользователем, напиши /duel\n" +
            "Чтобы выйти во время викторины, напиши /exit\n" +
            "Чтобы посмотреть статистику, напиши /stats\n" +
            "Чтобы посмотреть достижения, напиши /achievements";

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
            UserInteractionThreads.createThread(user, false);
            Logger.log(LogLevels.info,"Initialization: thread for user "+user.getChatId()+" started.");
        }
    }

    public void startUserInteraction() throws InterruptedException{
        botIO.println(helpText, chatId);
        resumeUserInteraction();
    }

    public void resumeUserInteraction() throws InterruptedException{
        while (true) {
            var userInput = botIO.readUserQuery(user);
            if (adminChatId != 0 && adminWantId == chatId)
                botIO.println(userInput, adminChatId);
            else
                UserCommandHandler.preQuizResolveCommand(userInput, user);
        }
    }
}