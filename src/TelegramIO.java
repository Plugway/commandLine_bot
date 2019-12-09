import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class TelegramIO implements IO {

    private static String apiKey = readApiKey(Main.ApiKeyPath);
    private TelegramBot bot = new TelegramBot(apiKey);

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
                return (int) UpdatesHandler.handleUpdates(updates);
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

    public String[] readDuelUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException
    {
        var input = new String[2];
        while (user1.messages.size() == 0 && user2.messages.size() == 0) {
            Thread.sleep(1000);
        }
        if (user1.messages.size() == 0)
        {
            if (user2.messages.peek().equals("/exit"))
                throw new DuelInterruptedException("desire,2");
            input[1] = user2.messages.poll();
            var res = handleDuelUserInput(user1, user2, 2);
            if (res.equals("/exit"))
                throw new DuelInterruptedException("desire,1");
            input[0] = res;
        }
        else
        {
            if (user1.messages.peek().equals("/exit"))
                throw new DuelInterruptedException("desire,1");
            input[0] = user1.messages.poll();
            var res = handleDuelUserInput(user2, user1, 1);
            if (res.equals("/exit"))
                throw new DuelInterruptedException("desire,2");
            input[1] = res;
        }

        System.out.println("Ввод от юзера с chatId " + user1.getChatId() + ":\n" + input[0]+"\nи ввод от юзера с chatId " + user2.getChatId() + ":\n" + input[1]);
        return input;
    }

    private String handleDuelUserInput(User user1, User user2, int respondedUser) throws InterruptedException, DuelInterruptedException
    {
        println("Ожидаем противника.", user2.getChatId());
        println("Ваш противник уже ответил. Поторопитесь :)", user1.getChatId());
        while (user1.messages.size() == 0)
        {
            if (user2.messages.size() != 0)
            {
                if (Objects.requireNonNull(user2.messages.poll()).equals("/exit"))
                    throw new DuelInterruptedException("timeout," + respondedUser);
                else
                {
                    println("Подождите еще немного, либо напишите /exit для того чтобы сдаться.", user2.getChatId());
                    println("Ваш противник негодует. Думайте быстрее!", user1.getChatId());
                }
            }
            Thread.sleep(1000);
        }
        return user1.messages.poll();
    }
    public static String readApiKey(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Error("Can't read api key from " + path);
        }
    }
    public static String getApiKey()
    {
        return apiKey;
    }
}