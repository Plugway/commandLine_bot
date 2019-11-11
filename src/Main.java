public class Main
{
    public static String QuestPath = "src/q&a.txt";
    public static String UsersPath = "src/users.txt";
    public static String UsersHashPath = "src/usersHash.txt";
    public static final BotIOType botMode = BotIOType.Telegram;

    public static void main(String[] args)
    {
        var botIO = BotIOFactory.getBotIO(botMode);

        UserTable.setTable( UserTableSerialization.deserialize(UsersPath) );
        if (!Hash.verifyHashFileAgainst(UsersHashPath, UsersPath))
            throw new RuntimeException("The Users file is broken. ");

        Logic.initializeAllUserThreads(botIO);

        System.out.println("started");
    }
}
