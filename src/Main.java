import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static final BotIOType botMode = BotIOType.Telegram;
    public static IO botIO = BotIOFactory.getBotIO(botMode);
    public static void main(String[] args) throws DeserializationException, WrongHashException, IOException
    {
        Logger.initializeLogger();
        UserTable.initializeUserTable();
        Question.parseQuestions();
        Logic.initializeAllUserThreads();
        AdminPanel.initializeAdminPanel();
        UserTableSerializer.runSerializer(10000);

        Logger.log(LogLevels.info,"Initialization finished, bot started.");
    }
}