
import java.io.IOException;

public class Main
{
    public static String QuestPath = "src/q&a.txt";
    public static String UsersPath = "src/users.txt";
    public static final BotIOType botMode = BotIOType.Telegram;

    public static void main(String[] args)
    {
        var botIO = BotIOFactory.getBotIO(botMode);

        UserTable.setTable( UserTableSerialization.deserialize(Main.UsersPath) );

        Logic.initializeAllUserThreads(botIO);

        System.out.println("started");
    }
}
