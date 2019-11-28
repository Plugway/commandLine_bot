public interface IO {
    String readUserQuery(User user) throws InterruptedException;
    String[] readDuelUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException;

    void println(String response, long chatId);
    void println(String response, long chatId1, long chatId2);
    void removeListener();
    void setListener();
}