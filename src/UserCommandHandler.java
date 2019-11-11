import java.io.IOException;

public class UserCommandHandler
{
    public static void resolveCommand(String command, User user, boolean quizIsRunning, IO botIO) throws IOException, InterruptedException, QuizShouldFinishException
    {
        switch (command) {
            case "/start":
                if (!quizIsRunning) {
                    new QuizLogic().runQuiz(user, botIO);
                }
                else
                    botIO.println("Вы не можете начать ещё одну викторину. Сначала завершите текущую.", user.getChatId());
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
