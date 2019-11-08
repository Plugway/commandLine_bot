import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserTableSerialization
{
    public static void serialize(List<User> userTable, String filePath)
    {
        try {
            ObjectSerialization.serialize(userTable, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to the file containing users' info");
        }
    }

    public static List<User> deserialize(String filePath)
    {
        List<User> resultAsList;
        Object resultAsObject = null;

        try {
            resultAsObject = ObjectSerialization.deserialize(filePath);
        } catch (FileNotFoundException e) {
            resultAsObject = new ArrayList<User>();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Unable to read the file containing users' info");
        }

        try {
            resultAsList = (List<User>) resultAsObject;
            if (resultAsList == null)
                throw new NullPointerException();
        } catch (ClassCastException | NullPointerException e) {     //does the ClassCastException actually do anything here?
            throw new RuntimeException("Unable to read the file containing users' info");
        }

        return resultAsList;
    }
}
