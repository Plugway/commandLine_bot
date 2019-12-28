import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserTable {

    public static void initializeUserTable() throws DeserializationException, WrongHashException {
        UserTable.userTable = UserTableSerialization.deserialize(FilePaths.UsersPath);
    }

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

    public static User getUserById(long id) {
        return userTable.stream()
                .filter(u -> u.getChatId() == id)
                .findFirst()
                .orElse(null);
    }

    public static synchronized void setUser(int index, User user) {
        userTable.set(index, user);
    }

    public static synchronized void add(User user) {
        userTable.add(user);
    }

    public static List<User> getUsersByUsername(String username) {
        return userTable.stream()
                .filter(u -> u.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public static List<User> getUsersByFirstname(String firstname) {
        return userTable.stream()
                .filter(u -> u.getFirstName().equals(firstname))
                .collect(Collectors.toList());
    }

    public static List<User> getUsersByLastname(String lastname) {
        return userTable.stream()
                .filter(u -> u.getLastName().equals(lastname))
                .collect(Collectors.toList());
    }

    public static List<User> getUsersByHighscore(String highscore) {
        return userTable.stream()
                .filter(u -> Integer.toString(u.getHighscore()).equals(highscore))
                .collect(Collectors.toList());
    }
    public static List<User> getUsersByType(FindTypes type, String query)
    {
        switch (type)
        {
            case chatId:
                var resultList=new ArrayList<User>();
                resultList.add(UserTable.getUserById(Long.parseLong(query)));
                return resultList;
            case firstname:
                return UserTable.getUsersByFirstname(query);
            case lastname:
                return UserTable.getUsersByLastname(query);
            case username:
                return UserTable.getUsersByUsername(query);
            case highscore:
                return UserTable.getUsersByHighscore(query);
            default:
                return new ArrayList<>();
        }
    }
}