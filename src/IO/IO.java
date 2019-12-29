import com.pengrad.telegrambot.model.request.Keyboard;

public interface IO {
    String readUserQuery(User user) throws InterruptedException;
    String[] readDuelUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException;

    void println(String response, Keyboards keyboard, long... chatId);
    void println(String response, long... chatId);
    void removeListener();
    void setListener();
}