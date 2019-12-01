import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class UpdatesHandler {
    public static long handleUpdates(List<Update> updates) throws SerializationException {
        var update = updates.get(0);
        if (update.message() == null)
            return update.updateId();
        var chat = update.message().chat();
        var user = UserTable.getUserById(chat.id());
        if (user != null) {
            if (update.message().text() != null)
                user.messages.add(update.message().text());
        } else {
            var newUser = new User(chat);
            UserTable.add(newUser);
            UserTableSerialization.serialize(UserTable.get(), Main.UsersPath);
            Hash.writeHashOfFileToFile(Main.UsersPath, Main.UsersHashPath);

            UserInteractionThreads.createThread(newUser, true);
        }
        return update.updateId();
    }
}