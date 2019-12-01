import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static String QuestPath = "data/q&a.txt";
    public static String UsersPath = "data/users.txt";
    public static String UsersHashPath = "data/usersHash.txt";
    public static String ApiKeyPath = "data/api.txt";
    public static final BotIOType botMode = BotIOType.Telegram;
    public static IO botIO = BotIOFactory.getBotIO(botMode);
    public static void main(String[] args) throws DeserializationException, WrongHashException, IOException {

        new UserTable(UserTableSerialization.deserialize(UsersPath));
        Question.parseQuestions(QuestPath);

        Logic.initializeAllUserThreads();

        System.out.println("started");
    }
}