import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TelegramIO implements IO {

    private static String apiKey = readApiKey(FilePaths.ApiKeyPath);
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
        bot.setUpdatesListener(updates -> (int) UpdatesHandler.handleUpdates(updates));
    }

    public String readUserQuery(User user) throws InterruptedException {
        while (user.messages.size() == 0) {
            Thread.sleep(1000);
        }
        var userInput = user.messages;
        Logger.log(LogLevels.info,"User input: "+ userInput.peek()+", chatId: "+user.getChatId());
        return userInput.poll();
    }

    public void println(String response, Keyboards keyboard, long... chatId) {
        for (long id : chatId)
            bot.execute(new SendMessage(id, response).replyMarkup(KeyboardsStorage.get(keyboard)));
        Logger.log(LogLevels.info,"Sent: " + response + ", keyboard: "+keyboard+", chatIds: "+ Arrays.toString(chatId));
    }
    public void println(String response, long... chatId) {
        for (long id : chatId)
            bot.execute(new SendMessage(id, response));
        Logger.log(LogLevels.info,"Sent: " + response + ", keyboard: empty, chatIds: "+ Arrays.toString(chatId));
    }

    public String[] readDuelUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException
    {
        var input = new String[2];
        while (user1.messages.size() == 0 && user2.messages.size() == 0) {
            Thread.sleep(1000);
        }
        if (user1.messages.size() == 0)
        {
            if (user2.messages.peek().equals("/exit")) {
                user2.messages.poll();
                throw new DuelInterruptedException(2, DuelInterruptedCause.desire);
            }
            input[1] = user2.messages.poll();
            var res = handleDuelUserInput(user1, user2, 2);
            if (res.equals("/exit"))
                throw new DuelInterruptedException(1, DuelInterruptedCause.desire);
            input[0] = res;
        }
        else
        {
            if (user1.messages.peek().equals("/exit")) {
                user1.messages.poll();
                throw new DuelInterruptedException(1, DuelInterruptedCause.desire);
            }
            input[0] = user1.messages.poll();
            var res = handleDuelUserInput(user2, user1, 1);
            if (res.equals("/exit"))
                throw new DuelInterruptedException(2, DuelInterruptedCause.desire);
            input[1] = res;
        }

        Logger.log(LogLevels.info,"User #1 input: "
                + input[0] + ", chatId: "
                + user1.getChatId()+"; user #2 input: "
                + input[1] + ", chatId: "
                + user2.getChatId());
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
                    throw new DuelInterruptedException(respondedUser, DuelInterruptedCause.timeout);
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
            Logger.log(LogLevels.fatal, "Can't read api key from " + path);
            throw new Error("Can't read api key from " + path);
        }
    }
    public static String getApiKey()
    {
        return apiKey;
    }
}