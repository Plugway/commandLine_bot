import jdk.jfr.Timespan;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Date;

public class CmdTelegramBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText() +"   "+ new Date(update.getMessage().getDate()).toString());
    }

    @Override
    public String getBotUsername() {
        return "CommandLine_Bot";
    }

    @Override
    public String getBotToken() {
        return "956241997:AAE1ePPGGpKbSj8j19yIDQ-3aZf6UKDmJ-I";
    }
}
