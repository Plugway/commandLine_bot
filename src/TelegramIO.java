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
    private TelegramBot bot = new TelegramBot(getApiKey());

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

        var userInput = new ArrayList<Queue<String>>();
        userInput.set(0, user1.messages);
        userInput.set(1, user1.messages);
        if (userInput.get(0).size() == 0)
            handleDuelUserInput(user1, user2, userInput, input, 0);
        else
            handleDuelUserInput(user1, user2, userInput, input, 1);

        System.out.println("Ввод от юзера с chatId " + user1.getChatId() + ":\n" + input[0]+"\nи ввод от юзера с chatId " + user2.getChatId() + ":\n" + input[1]);
        return input;
    }

    private void handleDuelUserInput(User user1, User user2, ArrayList<Queue<String>> userInput, String[] input, int whichUsr) throws InterruptedException, DuelInterruptedException
    {
        int otherUsr = whichUsr == 0? 1: 0;

        input[otherUsr] = userInput.get(otherUsr).poll();
        println("Ожидаем противника.", user2.getChatId());
        println("Ваш противник уже ответил. Поторопитесь :)", user1.getChatId());
        while (userInput.get(whichUsr).size() == 0)
        {
            if (userInput.get(otherUsr).size() != 0)
            {
                if (Objects.requireNonNull(userInput.get(otherUsr).poll()).equals("/exit"))
                    throw new DuelInterruptedException("timeout," + (otherUsr+1));
                else
                {
                    println("Подождите еще немного, либо напишите /exit для того чтобы сдаться.", user2.getChatId());
                    println("Ваш противник негодует. Думайте быстрее!", user1.getChatId());
                }
            }
            Thread.sleep(1000);
        }
        input[whichUsr] = userInput.get(whichUsr).poll();
    }
    public static String getApiKey() {
        String apiKeyPath = Main.ApiKeyPath;
        try {
            return Files.readString(Paths.get(apiKeyPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Error("Can't read api key from " + apiKeyPath);
        }
    }
}