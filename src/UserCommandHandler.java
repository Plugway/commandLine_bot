import java.io.IOException;

public class UserCommandHandler
{
    public static int resolveCommand(String command, User user, boolean questIsQuit) throws IOException, InterruptedException
    {
        switch (command)
        {
            case "/start":
                if (questIsQuit)
                {
                    //questIsQuit = false;
                    new QuizLogic(user).runQuiz();
                }
                break;
            case "/help":
                BotIO.getBotIO().println(Logic.getHelpText(), user.getChatId());
                break;
            case "/exit":
                return 1;      //awful AWFUL kludge to make sure quizLogic knows when to end execution
            default:
                BotIO.getBotIO().println("Я не знаю такой команды.", user.getChatId());
        }

        return 0;
    }
}
