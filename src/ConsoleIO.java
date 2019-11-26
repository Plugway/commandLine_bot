import java.util.Scanner;

public class ConsoleIO implements IO {
    private Scanner in = new Scanner(System.in);

    public String readUserQuery(User chatId) {
        return in.nextLine();
    }

    @Override
    public String[] readUsersQueries(User user1, User user2) throws InterruptedException, DuelInterruptedException {
        return new String[0];
    }

    public void println(String response, long chatId) {
        System.out.println(response);
    }

    @Override
    public void println(String response, long chatId1, long chatId2) {

    }

    @Override
    public void removeListener() {

    }

    @Override
    public void setListener() {

    }
}