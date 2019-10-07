import java.util.Scanner;

public class ConsoleIO implements IO
{
    private Scanner in = new Scanner(System.in);

    public String readUserQuery()
    {
        return in.nextLine();
    }

    public void println(String response)
    {
        System.out.println(response);
    }
}