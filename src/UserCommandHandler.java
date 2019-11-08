import java.io.IOException;

public class UserCommandHandler
{
    public static void resolveCommand(String command, User user, boolean quizIsRunning, IO botIO) throws IOException, InterruptedException, QuizShouldFinishException
    {
        switch (command) {
            case "/start":
                if (!quizIsRunning) {
                    new QuizLogic(user).runQuiz(botIO);
                }
                else
                    botIO.println("Вы не можете начать новую викторину, пока продолжается текущая.", user.getChatId());
                break;
            case "/help":
                botIO.println(Logic.getHelpText(), user.getChatId());
                break;
            case "/exit":
                throw new QuizShouldFinishException();
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }
    }
}
