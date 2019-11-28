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
}