import java.io.IOException;

public class UserCommandHandler {
    public static void preQuizResolveCommand(String command, User user) throws IOException, InterruptedException, SerializationException {
        var botIO = Main.botIO;
        switch (command) {
            case "/start":
                new QuizLogic().runQuiz(user, botIO);
                break;
            case "/help":
                botIO.println(Logic.getHelpText(), user.getChatId());
                break;
            case "/exit":
                botIO.println("Куда выходить? Викторину ещё даже не начали :)", user.getChatId());
                break;
            case "/admin":
                botIO.println("Введите ключ досупа:", user.getChatId());
                var response = botIO.readUserQuery(user);
                if (response.equals(Main.ApiKey))
                    new AdminPanel(user).run();
                break;
            case "/top":
                botIO.println(UserTable.getHighscoreTable(user), user.getChatId());
                break;
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }
    }

    public static void quizResolveCommand(String command, User user) throws QuizShouldFinishException {
        var botIO = Main.botIO;
        switch (command) {
            case "/start":
                botIO.println("Вы не можете начать ещё одну викторину. Сначала завершите текущую.", user.getChatId());
                break;
            case "/help":
                botIO.println(Logic.getHelpText(), user.getChatId());
                break;
            case "/exit":
                throw new QuizShouldFinishException();
            case "/top":
                botIO.println("Чтобы увидеть рекорды, завершите викторину.", user.getChatId());
                break;
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }
    }
}