import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class UpdatesHandler {
    public static long handleUpdates(List<Update> updates) {
        var update = updates.get(0);
        if (update.message() == null)
            return update.updateId();
        var chat = update.message().chat();
        var user = UserTable.getUserById(chat.id());
        if (user != null) {
            if (update.message().text() != null)
                user.messages.add(update.message().text());
            else
                user.getStats().addIgnoredMessagesCount(user, Main.botIO);
        } else {
            var newUser = new User(chat);
            UserTable.add(newUser);

            UserInteractionThreads.createThread(newUser, true);
        }
        return update.updateId();
    }
}