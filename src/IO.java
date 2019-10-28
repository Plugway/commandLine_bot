public interface IO
{
    String readUserQuery(User chatId) throws InterruptedException;
    void println (String response, long chatId);
}
