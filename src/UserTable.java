import java.util.List;

public class UserTable
{
    private static List<User> userTable;

    public static List<User> get()
    {
        return userTable;
    }
    public static User getUser(int index)
    {
        return userTable.get(index);
    }
    public static int getIndexOf(User user)
    {
        return userTable.indexOf(user);
    }
    public static boolean contains(User user)
    {
        return userTable.contains(user);
    }

    public static synchronized void setTable(List<User> newUserTable)
    {
        userTable = newUserTable;
    }
    public static synchronized void setUser(int index, User user)
    {
        userTable.set(index, user);
    }
    public static synchronized void add(User user)
    {
        userTable.add(user);
    }
}
