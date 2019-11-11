import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class TelegramIO implements IO
{
    private TelegramBot bot = new TelegramBot("00000");

    public TelegramIO() {
        bot.setUpdatesListener(updates -> (int) UpdatesHandler.handleUpdates(updates, this));
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
