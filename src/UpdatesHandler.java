import com.pengrad.telegrambot.model.Update;

import java.io.IOException;
import java.util.List;

public class UpdatesHandler
{
    public static long handleUpdates(List<Update> updates) throws IOException
    {
        var update = updates.get(0);
        var chat = update.message().chat();
        var user = new User(chat);
        if (User.userTable.contains(user))
        {
            if (update.message().text() != null)
                user.messages.add(update.message().text());
            User.userTable.add(user);
        }
        else
        {
            User.userTable.add(user);
            Serialization.serialize(User.userTable, Main.UsersPath);
            UserInteractionThreads.createThread(user, true);
        }
        return update.updateId();
    }
}
