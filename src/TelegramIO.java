import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TelegramIO implements IO {
    private TelegramBot bot = new TelegramBot(Main.ApiKey);

    public TelegramIO() {
        setListener();
    }

    public void removeListener()
    {
        bot.removeGetUpdatesListener();
    }
    public void setListener()
    {
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

    public void println(String response, long chatId1, long chatId2) {
        bot.execute(new SendMessage(chatId1, response));
        bot.execute(new SendMessage(chatId2, response));
        System.out.println("Выслано юзерам с chatId " + chatId1 + " и " + chatId2 + ":\n" + response);
    }

    public String[] readUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException {
        var input = new String[2];
        while (user1.messages.size() == 0 && user2.messages.size() == 0) {
            Thread.sleep(1000);
        }
        var user1Input = user1.messages;
        var user2Input = user2.messages;
        if (user1Input.size() == 0)
        {
            input[1] = user2Input.poll();
            println("Ожидаем противника.", user2.getChatId());
            println("Ваш противник уже ответил. Поторопитесь :)", user1.getChatId());
            while (user1Input.size() == 0)
            {
                if (user2Input.size() != 0)
                {
                    if (user2Input.poll().equals("/exit"))
                        throw new DuelInterruptedException("timeout,2");
                    else
                    {
                        println("Подождите еще немного, либо напишите /exit для того чтобы сдаться.", user2.getChatId());
                        println("Ваш противник негодует. Думайте быстрее!", user1.getChatId());
                    }
                }
                Thread.sleep(1000);
            }
            input[0] = user1Input.poll();
        }
        else
        {
            input[0] = user1Input.poll();
            println("Ожидаем противника.", user1.getChatId());
            println("Ваш противник уже ответил. Поторопитесь :)", user2.getChatId());
            while (user2Input.size() == 0)
            {
                if (user1Input.size() != 0)
                {
                    if (user1Input.poll().equals("/exit"))
                        throw new DuelInterruptedException("timeout,1");
                    else
                    {
                        println("Подождите еще немного, либо напишите /exit для того чтобы сдаться.", user1.getChatId());
                        println("Ваш противник негодует. Думайте быстрее!", user2.getChatId());
                    }
                }
                Thread.sleep(1000);
            }
            input[1] = user2Input.poll();
        }
        System.out.println("Ввод от юзера с chatId " + user1.getChatId() + ":\n" + input[0]+"и ввод от юзера с chatId " + user2.getChatId() + ":\n" + input[1]);
        return input;
    }
}