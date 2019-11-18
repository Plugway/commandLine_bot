public interface IO {
    String readUserQuery(User user) throws InterruptedException;

    void println(String response, long chatId);
}