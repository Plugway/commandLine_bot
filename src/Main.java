import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static final BotIOType botMode = BotIOType.Telegram;
    public static IO botIO = BotIOFactory.getBotIO(botMode);
    public static void main(String[] args) throws DeserializationException, WrongHashException, IOException
    {
        new UserTable(UserTableSerialization.deserialize(FilePaths.UsersPath));
        Question.parseQuestions(FilePaths.QuestPath);
        AdminPanel.setAdminPassword();
        Logic.initializeAllUserThreads();

        System.out.println("started");
    }
}