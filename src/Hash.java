import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
    public static boolean verifyHashFileAgainst(String hashFilePath, String file2Path)
    {
        byte[] file2Contents;
        try {
            file2Contents = readFileAsBytes(file2Path);
        } catch (IOException e) {
            System.out.println(file2Path + " doesn't exist. If it's the users table file, it will be created when a new user messages the bot. Thus assuming hash \"verification\" was correct.");
            return true;
        }
        var file2Hash = calculateNewHash(file2Contents);

        String hashFileContents;
        try {
            hashFileContents = new String(readFileAsBytes(hashFilePath));
        } catch (IOException e)
        {
            System.out.println(hashFilePath + " doesn't exist. If it's the users table hash file, it will be created when a new user messages the bot. Thus assuming hash \"verification\" was correct.");
            return true;
        }

        System.out.println("hashFileContents: " + hashFileContents + ", file2Hash " + file2Hash);
        return (hashFileContents.equals(file2Hash));
    }

    public static void writeHashOfFileToFile(String fileToCalculatePath, String hashFilePath)
    {
        byte[] hashFileContents;
        try {
            hashFileContents = readFileAsBytes(fileToCalculatePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file meant to calculate a hash.");
        }
        var hashToWrite = calculateNewHash(hashFileContents);

        writeFile(hashToWrite, hashFilePath);
    }

    private static byte[] readFileAsBytes(String filePath) throws IOException
    {
        var fileIn = new FileInputStream(filePath);
        byte[] fileContents = fileIn.readAllBytes();
        fileIn.close();
        return fileContents;
    }

    private static void writeFile(String contentToWrite, String filePath)
    {
        try {
            var fileOut = new FileOutputStream(filePath);
            fileOut.write(contentToWrite.getBytes());
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write hash to file.");
        }

        System.out.println("WROTE: " + contentToWrite + " to " + filePath);
    }

    private static String calculateNewHash(byte[] bytes)
    {
        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(bytes);
            hash = Hex.encodeHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate a hash.");
        }

        if (hash == null)
            throw new NullPointerException("Failed to calculate a hash.");

        return hash;
    }
}
