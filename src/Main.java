
import java.io.IOException;

public class Main
{
    public static String QuestPath = "src/q&a.txt";
    public static String UsersPath = "src/users.txt";
    public static final BotIOType botMode = BotIOType.Telegram;

    public static void main(String[] args) throws IOException, InterruptedException {
        var botIO = BotIOFactory.getBotIO(botMode);
        Logic.initializeAllUserThreads(botIO);

        System.out.println("started");
    }
}
