import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class UpdatesHandler
{
    public static long handleUpdates(List<Update> updates, IO botIO)
    {
        var update = updates.get(0);
        var chat = update.message().chat();
        var userIsPresent = false;
        for (User user : UserTable.get())
        {
            if (user.getChat().equals(chat))
            {
                if (update.message().text() != null)
                    user.messages.add(update.message().text());
                userIsPresent = true;
                break;
            }
        }
        if (!userIsPresent)
        {
            var newUser = new User(chat);
            UserTable.add(newUser);
            UserTableSerialization.serialize(UserTable.get(), Main.UsersPath);
            Hash.writeHashOfFileToFile(Main.UsersPath, Main.UsersHashPath);

            UserInteractionThreads.createThread(newUser, true, botIO);
        }
        return update.updateId();
    }
}
