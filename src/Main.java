import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static String QuestPath = "src/q&a.txt";
    public static String UsersPath = "src/users.txt";
    public static String UsersHashPath = "src/usersHash.txt";
    public static String ApiKey = getApiKey();
    public static final BotIOType botMode = BotIOType.Telegram;
    public static IO botIO;
    public static void main(String[] args) throws DeserializationException, SerializationException, WrongHashException, IOException {

        botIO = BotIOFactory.getBotIO(botMode);
        UserTable.setTable(UserTableSerialization.deserialize(UsersPath));
        Question.parseQuestions(QuestPath);

        Logic.initializeAllUserThreads();

        System.out.println("started");
    }

    private static String getApiKey() {
        String apiKeyPath = "src/api.txt";
        try {
            return Files.readString(Paths.get(apiKeyPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Error("Can't read api key from " + apiKeyPath);
        }
    }
}