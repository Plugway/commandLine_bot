import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Main
{
    public static String QuestPath = "src/q&a.txt";
    public static void main(String[] args) throws IOException {

        /*ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new CmdTelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/


        Logic.start();
    }
}
