import java.io.IOException;

public class UserCommandHandler
{
    public static int resolveCommand(String command, User user, boolean questIsQuit, IO botIO) throws IOException, InterruptedException
    {
        switch (command)
        {
            case "/start":
                if (questIsQuit)
                {
                    new QuizLogic(user).runQuiz(botIO);
                }
                break;
            case "/help":
                botIO.println(Logic.getHelpText(), user.getChatId());
                break;
            case "/exit":
                return 1;      //awful AWFUL kludge to make sure quizLogic knows when to end execution
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }

        return 0;
    }
}
