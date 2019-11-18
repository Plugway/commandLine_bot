import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserTableSerialization {
    public static void serialize(List<User> userTable, String filePath) throws SerializationException {
        try {
            ObjectSerialization.serialize(userTable, filePath);
        } catch (IOException e) {
            throw new SerializationException("Unable to write to the file containing users' info");
        }
    }

    public static List<User> deserialize(String filePath) throws DeserializationException, WrongHashException {
        List<User> resultAsList;
        Object resultAsObject = null;

        if (!Hash.verifyHashFileAgainst(Main.UsersHashPath, Main.UsersPath))
            throw new WrongHashException("The Users file is broken, hash doesn't match.");

        try {
            resultAsObject = ObjectSerialization.deserialize(filePath);
        } catch (FileNotFoundException e) {
            resultAsObject = new ArrayList<User>();
        } catch (ClassNotFoundException | IOException e) {
            throw new DeserializationException("Unable to read the file containing users' info");
        }

        try {
            resultAsList = (List<User>) resultAsObject;
            if (resultAsList == null)
                throw new NullPointerException();
        } catch (ClassCastException | NullPointerException e) {     //does the ClassCastException actually do anything here?
            throw new DeserializationException("Unable to read the file containing users' info");
        }

        return resultAsList;
    }
}