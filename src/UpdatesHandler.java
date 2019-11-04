import com.pengrad.telegrambot.model.Update;

import java.io.IOException;
import java.util.List;

public class UpdatesHandler
{
    public static long handleUpdates(List<Update> updates, IO botIO) throws IOException
    {
        var update = updates.get(0);
        var chat = update.message().chat();
        var user = new User(chat);
        if (User.userTable.contains(user))
        {
            var index = User.userTable.indexOf(user);       //why is this line necessary?
            var user2 = User.userTable.get(index);          //why is this line necessary? WHY
            if (update.message().text() != null)
                user2.messages.add(update.message().text());
            User.userTable.set(index, user2);               //why is this line necessary? WHY???
        }
        else
        {
            var newUser = new User(chat);
            User.userTable.add(newUser);
            Serialization.serialize(User.userTable, Main.UsersPath);
            UserInteractionThreads.createThread(newUser, true, botIO);
        }
        return update.updateId();
    }
}
