import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminPanel {
    private static String adminPassword;
    public static String getAdminPassword(){return adminPassword;}
    public static void setAdminPassword(){adminPassword = getAdminPassword(Main.AdminPanelPasswordPath);}

    public AdminPanel(User user){
        this.user = user;
    }
    private User user;
    private IO botIO = Main.botIO;
    public void run() throws InterruptedException {
        var mainMenuCommands="Доступные команды:\n1./find\n2./sendAll\n3./sleep\n4./exit";
        botIO.println("Добро пожаловать.\n" + mainMenuCommands, user.getChatId());
        while (true)
        {
            var userCommand = botIO.readUserQuery(user);
            switch (userCommand)
            {
                case "/find":
                    runFindDial();
                    printHelpText(MenuHelpText.adminMain);
                    break;
                case "/sendAll":
                    runSendAllDial();
                    printHelpText(MenuHelpText.adminMain);
                    break;
                case "/sleep":
                    runSleepDial();
                    printHelpText(MenuHelpText.adminMain);
                    break;
                case "/exit":
                    return;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }
    private void runSleepDial() throws InterruptedException {
        botIO.println("Введите время сна в формате HH:mm:ss или /exit чтобы выйти.\nВнимание! Во время сна бот не будет отвечать!", user.getChatId());
        long time;
        while (true)
        {
            var input = botIO.readUserQuery(user);
            if (input.equals("/exit"))
                return;
            try {
                time = parseTimeToSleep(input);
                break;
            }
            catch (ParseException e)
            {
                botIO.println("Неправильное время, повторите ввод.", user.getChatId());
            }
        }
        botIO.removeListener();
        Thread.sleep(time);
        botIO.setListener();
        botIO.println("Время сна вышло.", user.getChatId());
    }
    private long parseTimeToSleep(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date dt = formatter.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return ((cal.get(Calendar.HOUR)
                *60+cal.get(Calendar.MINUTE))
                *60+cal.get(Calendar.SECOND))*1000;
    }
    private void printHelpText(MenuHelpText type)
    {
        var mainMenuCommands="Доступные команды:\n1./find\n2./sendAll\n3./sleep\n4./exit";
        var findDialText = "Меню поиска участников.\nДоступные команды:\n1./chatId\n2./username\n3./firstname\n4./lastname\n5./highscore\n6./exit";
        switch (type)
        {
            case adminMain:
                botIO.println("Вы в главном меню.\n" + mainMenuCommands, user.getChatId());
                break;
            case adminFindDial:
                botIO.println(findDialText, user.getChatId());
                break;
            case adminInteractUser:
                botIO.println("Меню взаимодействия с пользователем.\nДоступные команды:\n1./talk\n2./exit", user.getChatId());
        }
    }
    private void runSendAllDial() throws InterruptedException {
        botIO.println("Введите сообщение для рассылки всем пользователям или /exit чтобы выйти.", user.getChatId());
        var message = botIO.readUserQuery(user);
        if (message.equals("/exit"))
            return;
        var users = UserTable.get();
        for (User user1 : users)
        {
            if (user1.equals(user))
                continue;
            botIO.println(message, user1.getChatId());
        }
    }
    private void runFindDial() throws InterruptedException {
        var findDialText = "Меню поиска участников.\nДоступные команды:\n1./chatId\n2./username\n3./firstname\n4./lastname\n5./highscore\n6./exit";
        botIO.println(findDialText, user.getChatId());
        while (true)
        {
            var userCommand = botIO.readUserQuery(user);
            switch (userCommand)
            {
                case "/chatId":
                    findType(FindTypes.chatId);
                    printHelpText(MenuHelpText.adminFindDial);
                    break;
                case "/username":
                    findType(FindTypes.username);
                    printHelpText(MenuHelpText.adminFindDial);
                    break;
                case "/firstname":
                    findType(FindTypes.firstname);
                    printHelpText(MenuHelpText.adminFindDial);
                    break;
                case "/lastname":
                    findType(FindTypes.lastname);
                    printHelpText(MenuHelpText.adminFindDial);
                    break;
                case "/highscore":
                    findType(FindTypes.highscore);
                    printHelpText(MenuHelpText.adminFindDial);
                    break;
                case "/exit":
                    return;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }
    private void findType(FindTypes type) throws InterruptedException {
        botIO.println("Введите значение:", user.getChatId());
        var input = botIO.readUserQuery(user);
        var resultList = UserTable.getUsersByType(type, input);
        var listToPrint = User.getListToPrint(resultList);
        while (true)
        {
            botIO.println(listToPrint, user.getChatId());
            botIO.println("Введите номер пользователя чтобы увидеть полную информацию или /exit", user.getChatId());
            int userNum;
            while (true)
            {
                input = botIO.readUserQuery(user);
                if (input.equals("/exit"))
                    return;
                try {
                    userNum = handleUserInput(0, resultList.size(), input);
                    interactUser(resultList.get(userNum));
                    break;
                }
                catch (Exception e)
                {
                    botIO.println("Ввод не удавлетворяет условиям. Повторите ввод.", user.getChatId());
                }
            }
        }
    }
    private int handleUserInput(int min, int max, String input) throws Exception {
        var res = Integer.parseInt(input)-1;
        if (res >= max || res < min)
            throw new Exception();
        return res;
    }
    private void interactUser(User gettedUser) throws InterruptedException {
        botIO.println(gettedUser.getDetalizedToPrint(), user.getChatId());
        printHelpText(MenuHelpText.adminInteractUser);
        while (true)
        {
            var input = botIO.readUserQuery(user);
            switch (input)
            {
                case "/talk":
                    dialogueWUser(gettedUser);
                    printHelpText(MenuHelpText.adminInteractUser);
                    break;
                case "/exit":
                    return;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }

    private void dialogueWUser(User gettedUser) throws InterruptedException {
        botIO.println("Для выхода из диалога напишите /exit.", user.getChatId());
        Logic.setAdminId(user.getChatId(), gettedUser.getChatId());
        while (true)
        {
            var input = botIO.readUserQuery(user);
            if (input.equals("/exit"))
            {
                Logic.setAdminId(0, 0);
                return;
            }
            else
                botIO.println(input, gettedUser.getChatId());
        }
    }

    private static String getAdminPassword(String path)
    {
        try {
            var pass = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
            System.out.println("Password read successfully: " + pass);
            return pass;
        } catch (IOException e) {
            System.out.println("Can't read admin pass from " + path + ".\nNow admin pass equals api key.");
            return TelegramIO.getApiKey();
        }
    }
}