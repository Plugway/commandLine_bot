import java.io.*;

public class Serialization {

    public static void serialize(Object o, String filePath) throws IOException {
        var fileOut = new FileOutputStream(filePath, false);
        var out = new ObjectOutputStream(fileOut);
        out.writeObject(o);
        out.close();
        fileOut.close();
    }

    public static Object deserialize(String filePath) throws IOException, ClassNotFoundException {
        var fileIn = new FileInputStream(filePath);
        var in = new ObjectInputStream(fileIn);
        var res = in.readObject();
        in.close();
        fileIn.close();
        return res;
    }
}
