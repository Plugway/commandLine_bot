public class UserInteractionThreads {
    public static void createThread(User user, boolean isNewUser) {
        var thread = new Thread(() -> {
            try {
                if (isNewUser)
                    new Logic(user).startUserInteraction();
                else
                    new Logic(user).resumeUserInteraction();
            } catch (InterruptedException e) {
                Logger.log(LogLevels.fatal, "Thread creation: thread crashed, userId " + user.getChatId());
                throw new RuntimeException("New thread could not be created.");
            }
        });
        thread.start();
    }
}