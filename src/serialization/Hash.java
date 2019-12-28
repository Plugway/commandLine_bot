import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
    public static boolean verifyHashFileAgainst(String hashFilePath, String file2Path) {
        byte[] file2Contents;
        try {
            file2Contents = readFileAsBytes(file2Path);
        } catch (IOException e) {
            Logger.log(LogLevels.warn, "Initialization: "+file2Path + " doesn't exist. If it's the users table file, it will be created when a new user messages the bot. Thus assuming hash \"verification\" was correct.");
            return true;
        }
        String file2Hash;
        try {
            file2Hash = calculateNewHash(file2Contents);
        } catch (NoSuchAlgorithmException e)
        {
            Logger.log(LogLevels.fatal, "Deserialization: Failed to calculate a hash.");
            return false;
        }
        String hashFileContents;
        try {
            hashFileContents = new String(readFileAsBytes(hashFilePath));
        } catch (IOException e) {
            Logger.log(LogLevels.warn, "Initialization: "+hashFilePath + " doesn't exist. If it's the users table hash file, it will be created when a new user messages the bot. Thus assuming hash \"verification\" was correct.");
            return true; //eeh
        }
        Logger.log(LogLevels.info, "Initialization: HashFileContents: " + hashFileContents + ", file2Hash " + file2Hash);
        return (hashFileContents.equals(file2Hash));
    }

    public static void writeHashOfFileToFile(String fileToCalculatePath, String hashFilePath) throws SerializationException {
        byte[] hashFileContents;
        try {
            hashFileContents = readFileAsBytes(fileToCalculatePath);
            var hashToWrite = calculateNewHash(hashFileContents);
            writeFile(hashToWrite, hashFilePath);
        } catch (IOException | NoSuchAlgorithmException e) {
            Logger.log(LogLevels.error, "Deserialization: Failed to read file meant to calculate a hash.");
            throw new SerializationException("Failed to read file meant to calculate a hash.");
        }
    }

    private static byte[] readFileAsBytes(String filePath) throws IOException {
        var fileIn = new FileInputStream(filePath);
        byte[] fileContents = fileIn.readAllBytes();
        fileIn.close();
        return fileContents;
    }

    private static void writeFile(String contentToWrite, String filePath) throws SerializationException {
        try {
            var fileOut = new FileOutputStream(filePath);
            fileOut.write(contentToWrite.getBytes());
            fileOut.close();
        } catch (IOException e) {
            Logger.log(LogLevels.error, "Serialization: Failed to write hash to file.");
            throw new SerializationException("Failed to write hash to file.");
        }

        // System.out.println("WROTE: " + contentToWrite + " to " + filePath);
    }

    private static String calculateNewHash(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(bytes);
        return Hex.encodeHexString(digest.digest());
    }
}