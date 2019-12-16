public class UserTableSerializer {
    public static void runSerializer(long delay)
    {
        var thread = new Thread(() -> {
            while (true)
            {
                try {
                    UserTableSerialization.serialize(UserTable.get(), FilePaths.UsersPath);
                    Hash.writeHashOfFileToFile(FilePaths.UsersPath, FilePaths.UsersHashPath);
                    Thread.sleep(delay);
                } catch (SerializationException | InterruptedException e) {
                    System.out.println("UTSerializer: Error: can't serialize.");
                }
            }
        });
        thread.start();
    }
}
