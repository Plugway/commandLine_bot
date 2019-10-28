
import java.io.IOException;

public class Main
{
    public static String QuestPath = "src/q&a.txt";
    public static String UsersPath = "src/users.txt";
    public static final BotType botMode = BotType.Telegram;

    public static void main(String[] args) throws IOException, InterruptedException {
        BotIO.selectIOClass();
        Logic.initializeAllUserThreads();

        System.out.println("started");
    }
}
