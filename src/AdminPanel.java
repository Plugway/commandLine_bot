import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel {
    public AdminPanel(User user){
        this.user = user;
    }
    private User user;
    private IO botIO = Main.botIO;
    public void run() throws InterruptedException {
        var exitFlag = true;
        var mainMenuCommands="Доступные команды:\n1./find\n2./exit";
        botIO.println("Добро пожаловать.\n" + mainMenuCommands, user.getChatId());
        while (exitFlag)
        {
            var userCommand = botIO.readUserQuery(user);
            switch (userCommand)
            {
                case "/find":
                    runFindDial();
                    botIO.println("Вы в главном меню.\n" + mainMenuCommands, user.getChatId());
                    break;
                case "/exit":
                    exitFlag=false;
                    break;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }
    private void runFindDial() throws InterruptedException {
        var exitFlag = true;
        var findDialText = "Меню поиска участников.\nДоступные команды:\n1./chatId\n2./username\n3./firstname\n4./lastname\n5./highscore\n6./exit";
        botIO.println(findDialText, user.getChatId());
        while (exitFlag)
        {
            var userCommand = botIO.readUserQuery(user);
            switch (userCommand)
            {
                case "/chatId":
                    findType(FindTypes.chatId);
                    botIO.println(findDialText, user.getChatId());
                    break;
                case "/username":
                    findType(FindTypes.username);
                    botIO.println(findDialText, user.getChatId());
                    break;
                case "/firstname":
                    findType(FindTypes.firstname);
                    botIO.println(findDialText, user.getChatId());
                    break;
                case "/lastname":
                    findType(FindTypes.lastname);
                    botIO.println(findDialText, user.getChatId());
                    break;
                case "/highscore":
                    findType(FindTypes.highscore);
                    botIO.println(findDialText, user.getChatId());
                    break;
                case "/exit":
                    exitFlag=false;
                    break;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }
    private void findType(FindTypes type) throws InterruptedException {
        botIO.println("Введите значение:", user.getChatId());
        var value = botIO.readUserQuery(user);
        List<User> resultList = new ArrayList<User>();
        var exitFlag = true;
        switch (type)
        {
            case chatId:
                //resultList = UserTable.getUserById(value);
                break;
            case firstname:
                resultList = UserTable.getUsersByFirstname(value);
                break;
            case lastname:
                resultList = UserTable.getUsersByLastname(value);
                break;
            case username:
                resultList = UserTable.getUsersByUsername(value);
                break;
            case highscore:
                resultList = UserTable.getUsersByHighscore(value);
                break;
        }
        var listToPrint = getListToPrint(resultList);
        while (exitFlag)
        {
            botIO.println(listToPrint, user.getChatId());
            botIO.println("Введите номер пользователя чтобы увидеть полную информацию или /exit", user.getChatId());
            int valNum = 0;
            while (true)
            {
                value = botIO.readUserQuery(user);
                if (value.equals("/exit"))
                {
                    exitFlag = false;
                    break;
                }
                try {
                    valNum = Integer.parseInt(value)-1;
                    if (valNum >= resultList.size() || valNum < 0)
                        throw new Exception();
                    interactUser(resultList.get(valNum));
                    break;
                }
                catch (Exception e)
                {
                    botIO.println("Ввод не удавлетворяет условиям. Повторите ввод.", user.getChatId());
                }
            }
        }
    }
    private void interactUser(User gettedUser) throws InterruptedException {
        var exitFlag = true;
        botIO.println(getUserToPrint(gettedUser), user.getChatId());
        botIO.println("Меню взаимодействия с пользователем.\nДоступные команды:\n1./talk\n2./exit", user.getChatId());
        while (exitFlag)
        {
            var input = botIO.readUserQuery(user);
            switch (input)
            {
                case "/talk":
                    dialogueWUser(gettedUser);
                    botIO.println("Меню взаимодействия с пользователем.\nДоступные команды:\n1./talk\n2./exit", user.getChatId());
                    break;
                case "/exit":
                    exitFlag = false;
                    break;
                default:
                    botIO.println("Неверная команда.", user.getChatId());
            }
        }
    }
    private void dialogueWUser(User gettedUser) throws InterruptedException {
        botIO.println("Для выхода из диалога напишите /exit.", user.getChatId());
        var exitFlag = true;
        Logic.setAdminId(user.getChatId(), gettedUser.getChatId());
        while (exitFlag)
        {
            var input = botIO.readUserQuery(user);
            if (input.equals("/exit"))
            {
                Logic.setAdminId(0, 0);
                exitFlag = false;
            }
            else
            {
                botIO.println(input, gettedUser.getChatId());
            }
        }
    }
    private String getUserToPrint(User gettedUser)
    {
        var builder = new StringBuilder();
        builder.append("Информация:\n");
        builder.append("Firstname: ").append(gettedUser.getFirstName()).append("\n");
        builder.append("Lastname: ").append(gettedUser.getLastName()).append("\n");
        builder.append("Username: @").append(gettedUser.getUsername()).append("\n");
        builder.append("ChatId: ").append(gettedUser.getChatId()).append("\n");
        builder.append("Highscore: ").append(gettedUser.getHighscore());
        return builder.toString();
    }
    private String getListToPrint(List<User> users){
        var builder = new StringBuilder();
        var linesToPrint = users.size();
        builder.append("Найдено:\n");
        for (var i = 0; i < linesToPrint; i++)
        {
            var usr = users.get(i);
            builder.append(i+1).append(".");
            builder.append(" ").append(usr.getFirstName());
            if (usr.getLastName() != null)
                builder.append(" ").append(usr.getLastName());
            if (usr.getUsername() != null)
                builder.append("(@").append(usr.getUsername()).append(")");
            builder.append(" - ").append(usr.getHighscore()).append("\n");
        }
        return builder.toString();
    }
}