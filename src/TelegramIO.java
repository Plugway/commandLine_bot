import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;

public class TelegramIO implements IO {

    private TelegramBot bot = new TelegramBot("00000");

    public TelegramIO() {
        bot.setUpdatesListener(updates -> {
            try {
                return (int) UpdatesHandler.handleUpdates(updates);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;       //the point of this return statement? didn't need it until added a try/catch
        });
    }
    public String readUserQuery(User user) throws InterruptedException {
        var index = User.userTable.indexOf(user);
        while (User.userTable.get(index).messages.size() == 0)
        {
            Thread.sleep(1000);
        }
        var usr = User.userTable.get(index);
        var usrInput = usr.messages;
        System.out.println("Введено:\n"+usrInput.peek()+ "\n ChatId=" + usr.getChatId());
        return usrInput.poll();
    }

    public void println(String response, long chatId) {
        bot.execute(new SendMessage(chatId, response));
        System.out.println("Выслано:\n"+response+"\n ChatId="+chatId);
    }
}
