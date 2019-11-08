import java.io.IOException;

public class UserInteractionThreads
{
    public static void createThread (User user, boolean isNewUser, IO botIO)
    {
        var thread = new Thread(() -> {
            try {
                if (isNewUser)
                    new Logic(user).startUserInteraction(botIO);
                else
                    new Logic(user).resumeUserInteraction(botIO);
            } catch (InterruptedException | IOException e){
                e.printStackTrace();
                throw new RuntimeException("New thread could not be created.");
            }
        });
        thread.start();
    }

}
