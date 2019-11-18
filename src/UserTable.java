import com.pengrad.telegrambot.model.Chat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserTable {
    private static List<User> userTable;

    public static List<User> get() {
        return userTable;
    }

    public static User getUser(int index) {
        return userTable.get(index);
    }

    public static int getIndexOf(User user) {
        return userTable.indexOf(user);
    }

    public static boolean contains(User user) {
        return userTable.contains(user);
    }

    public static User getUserById(Chat chat) {
        var user = new User(chat);
        var index = userTable.indexOf(user);
        if (index == -1) {
            return null;
        } else {
            return userTable.get(index);
        }
    }

    public static synchronized void setTable(List<User> newUserTable) {
        userTable = newUserTable;
    }

    public static synchronized void setUser(int index, User user) {
        userTable.set(index, user);
    }

    public static synchronized void add(User user) {
        userTable.add(user);
    }

    public static List<User> getUsersByUsername(String username) {
        return userTable.stream().filter(u -> u.getUsername().equals(username)).collect(Collectors.toList());
    }

    public static List<User> getUsersByFirstname(String firstname) {
        return userTable.stream().filter(u -> u.getFirstName().equals(firstname)).collect(Collectors.toList());
    }

    public static List<User> getUsersByLastname(String lastname) {
        return userTable.stream().filter(u -> u.getLastName().equals(lastname)).collect(Collectors.toList());
    }

    public static List<User> getUsersByHighscore(String highscore) {
        return userTable.stream().filter(u -> Integer.toString(u.getHighscore()).equals(highscore)).collect(Collectors.toList());
    }


    public static String getHighscoreTable(User user) {
        var highscores = userTable.stream()
                .filter((u) -> u.getHighscore() != 0)
                .sorted(Comparator.comparing(User::getHighscore).reversed())
                .collect(Collectors.toList());
        var builder = new StringBuilder();
        var linesToPrint = 10;
        if (highscores.size() < linesToPrint)
            linesToPrint = highscores.size();
        builder.append("Таблица рекордов:\n");
        for (var i = 0; i < linesToPrint; i++) {
            var usr = highscores.get(i);
            switch (i + 1) {
                case 1:
                    builder.append("\uD83E\uDD47");
                    break;
                case 2:
                    builder.append("\uD83E\uDD48");
                    break;
                case 3:
                    builder.append("\uD83E\uDD49");
                    break;
                default:
                    builder.append(i + 1).append(".");
            }
            builder.append(" ").append(usr.getFirstName());
            if (usr.getLastName() != null)
                builder.append(" ").append(usr.getLastName());
            if (usr.getUsername() != null)
                builder.append("(@").append(usr.getUsername()).append(")");
            builder.append(" - ").append(usr.getHighscore()).append("\n");
        }
        if (highscores.indexOf(user) == -1)
            builder.append("\nПока что у вас нет рекорда.");
        else {
            var normUser = highscores.get(highscores.indexOf(user)); //костыль, поправить
            builder.append("\nВы на ")
                    .append(highscores.indexOf(user) + 1)
                    .append(" месте со счетом ")
                    .append(normUser.getHighscore())
                    .append(".");
        }
        return builder.toString();
    }
}