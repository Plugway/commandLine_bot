import java.util.Scanner;

public class ConsoleIO implements IO {
    private Scanner in = new Scanner(System.in);

    public String readUserQuery(User chatId) {
        return in.nextLine();
    }

    @Override
    public String[] readDuelUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException {
        return new String[0];
    }

    @Override
    public void println(String response, Keyboards keyboard, long... chatId) {

    }
    public void println(String response, long... chatId) { }

    public void println(String response, long chatId) {
        System.out.println(response);
    }

    @Override
    public void removeListener() {

    }

    @Override
    public void setListener() {

    }
}