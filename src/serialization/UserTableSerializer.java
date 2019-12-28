public class UserTableSerializer {
    public static void runSerializer(long delay)
    {
        var thread = new Thread(() -> {
            while (true)
            {
                try {
                    Thread.sleep(delay);
                    UserTableSerialization.serialize(UserTable.get(), FilePaths.UsersPath);
                    Hash.writeHashOfFileToFile(FilePaths.UsersPath, FilePaths.UsersHashPath);
                    Logger.flushFW();           //здесь происходит запись в текстовый файл из буфера
                } catch (SerializationException | InterruptedException e) {
                    Logger.log(LogLevels.error, "UTSerializer: can't serialize.");
                }
            }
        });
        thread.start();
    }
}
