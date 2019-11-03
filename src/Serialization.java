import com.pengrad.telegrambot.model.Chat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serialization
{
    public static void serialize(Object o, String filePath) throws IOException
    {
        var fileOut = new FileOutputStream(filePath, false);
        var out = new ObjectOutputStream(fileOut);
        out.writeObject(o);
        out.close();
        fileOut.close();
    }

    public static Object deserialize(String filePath) throws IOException, ClassNotFoundException
    {
        try {
            var fileIn = new FileInputStream(filePath);
            var in = new ObjectInputStream(fileIn);
            var res = in.readObject();
            in.close();
            fileIn.close();
            return res;
        } catch (IOException e) {
            var fileOut = new FileOutputStream(filePath);
            fileOut.write(new byte[0]);
            fileOut.close();
            return new ArrayList<User>();
        }
    }
}
