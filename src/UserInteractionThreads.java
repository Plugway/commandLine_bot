import java.io.IOException;

public class UserInteractionThreads
{
    public static void createThread (User user, boolean isNewUser)
    {
        var thread = new Thread(() -> {
            try {
                if (isNewUser)
                    new Logic(user).startUserInteraction();
                else
                    new Logic(user).resumeUserInteraction();
            } catch (InterruptedException | IOException e){
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
