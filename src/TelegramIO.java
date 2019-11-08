import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;

public class TelegramIO implements IO
{
    private TelegramBot bot = new TelegramBot("956241997:AAE1ePPGGpKbSj8j19yIDQ-3aZf6UKDmJ-I");

    public TelegramIO() {
        bot.setUpdatesListener(updates -> {
            try {
                return (int) UpdatesHandler.handleUpdates(updates, this);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });
    }

    public String readUserQuery(User user) throws InterruptedException {
        while (user.messages.size() == 0)
        {
            Thread.sleep(1000);
        }
        var userInput = user.messages;
        System.out.println("Ввод от юзера с chatId " + user.getChatId() + ":\n" +userInput.peek());
        return userInput.poll();
    }

    public void println(String response, long chatId) {
        bot.execute(new SendMessage(chatId, response));
        System.out.println("Выслано юзеру с chatId " + chatId + ":\n" + response);
    }
}
