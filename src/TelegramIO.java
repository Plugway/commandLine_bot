import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.Serializable;

public class TelegramIO implements IO, Serializable {
    private TelegramBot bot = new TelegramBot(Main.ApiKey);

    public TelegramIO() {
        bot.setUpdatesListener(updates -> {
            try {
                return (int) UpdatesHandler.handleUpdates(updates, Main.botIO);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public String readUserQuery(User user) throws InterruptedException {
        while (user.messages.size() == 0) {
            Thread.sleep(1000);
        }
        var userInput = user.messages;
        System.out.println("Ввод от юзера с chatId " + user.getChatId() + ":\n" + userInput.peek());
        return userInput.poll();
    }

    public void println(String response, long chatId) {
        bot.execute(new SendMessage(chatId, response));
        System.out.println("Выслано юзеру с chatId " + chatId + ":\n" + response);
    }
}