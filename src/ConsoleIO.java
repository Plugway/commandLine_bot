import java.util.Scanner;

public class ConsoleIO implements IO
{
    private Scanner in = new Scanner(System.in);

    public String readUserQuery(User chatId)
    {
        return in.nextLine();
    }

    public void println(String response, long chatId)
    {
        System.out.println(response);
    }
}