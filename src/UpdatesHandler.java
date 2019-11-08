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
        if (UserTable.contains(user))
        {
            var index = UserTable.getIndexOf(user);        //why is this line necessary?
            var user2 = UserTable.getUser(index);          //why is this line necessary? WHY
            if (update.message().text() != null)
                user2.messages.add(update.message().text());
            UserTable.setUser(index, user2);               //why is this line necessary? WHY???
        }
        else
        {
            var newUser = new User(chat);
            UserTable.add(newUser);
            UserTableSerialization.serialize(UserTable.get(), Main.UsersPath);
            UserInteractionThreads.createThread(newUser, true, botIO);
        }
        return update.updateId();
    }
}
