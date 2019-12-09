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
            case "/duel":
                Duels.enterDuel(user, botIO);
                break;
            case "/admin":
                botIO.println("Введите ключ досупа:", user.getChatId());
                var response = botIO.readUserQuery(user);
                if (response.equals(AdminPanel.getAdminPassword()))
                    new AdminPanel(user).run();
                else
                    botIO.println("Неверный пароль.", user.getChatId());
                break;
            case "/top":
                botIO.println(Highscore.generateTable(user), user.getChatId());
                break;
            case "/achievements":
                botIO.println(user.getAchievementsListToPrint(), user.getChatId());
                break;
            case "/stats":
                botIO.println(user.getStats().toString(), user.getChatId());
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
            case "/duel":
                botIO.println("Чтобы войти в лобби, сначала завершите викторину.", user.getChatId());
                break;
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }
    }

    public static void preDuelResolveCommand(String command, User user)
    {
        var botIO = Main.botIO;
        switch (command) {
            case "/start":
                botIO.println("Вы не можете начать викторину, пока находитесь в лобби.", user.getChatId());
                break;
            case "/help":
                botIO.println("Ждём, когда найдётся противник.", user.getChatId());
                break;
            //case "/exit":
                //throw new ExitingLobbyException();
            case "/top":
                botIO.println("Вы не можете смотреть рекорды, пока находитесь в лобби.", user.getChatId());
                break;
            case "/duel":
                botIO.println("Вы уже находитесь в лобби.", user.getChatId());
                break;
            default:
                botIO.println("Я не знаю такой команды.", user.getChatId());
        }
    }
}