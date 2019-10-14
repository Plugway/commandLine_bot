import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.PriorityQueue;
import java.util.Queue;

public class TelegramIO implements IO {

    private TelegramBot bot = new TelegramBot("957698997:AAE1ePPdcsuhgudfihoadmoadniUKDmJ-I");
    private Queue<String> userInput = new PriorityQueue<>();
    private long chatId;
    public TelegramIO()
    {
        bot.setUpdatesListener(updates -> {
            var update = updates.get(updates.size()-1);
            chatId = update.message().chat().id();
            userInput.add(update.message().text());
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
    public String readUserQuery() throws InterruptedException {
        while (userInput.size()==0)
        { Thread.sleep(300);
        }
        System.out.println("Введено:\n"+userInput.peek());
        return userInput.poll();
    }

    public void println(String response) {
        SendResponse resp = bot.execute(new SendMessage(chatId, response));
        System.out.println("Выслано:\n"+response);
    }
}
