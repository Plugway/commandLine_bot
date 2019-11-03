import com.pengrad.telegrambot.model.Update;
import java.io.*;
import java.util.*;

public class Logic
{
    public Logic(User user)
    {
        this.user = user;
        chatId = user.getChatId();
    }

    private static String helpText = "Привет, я бот, вот что я умею:\n" +
            "1. Задавать вопросики, на которые тебе нужно ответить.\n\n" +
            "Чтобы начать, напиши /start\n" +
            "Для вызова помощи напиши /help\n" +
            "Чтобы выйти во время викторины напиши /exit";
    public static String getHelpText() { return helpText; }

    private User user;
    private long chatId;

    public static void initializeAllUserThreads()
    {
        for (User user : User.userTable)
            UserInteractionThreads.createThread(user, false);
    }

    public void startUserInteraction() throws IOException, InterruptedException
    {
        BotIO.getBotIO().println(helpText, chatId);
        while (true) {
            UserCommandHandler.resolveCommand(BotIO.getBotIO().readUserQuery(user), user, true);   //that "true" that gets passed feels like a kludge
        }
    }

    public void resumeUserInteraction() throws IOException, InterruptedException
    {
        while (true) {
            UserCommandHandler.resolveCommand(BotIO.getBotIO().readUserQuery(user), user, true);   //that "true" that gets passed feels like a kludge
        }
    }
}
