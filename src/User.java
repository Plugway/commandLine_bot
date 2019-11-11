import com.pengrad.telegrambot.model.Chat;

import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Queue;

public class User implements Serializable {
    public User(Chat chat)
    {
        username = chat.username();
        firstName = chat.firstName();
        lastName = chat.lastName();
        chatId = chat.id();
        this.chat = chat;
    }

    public Queue<String> messages = new PriorityQueue<>();
    private String username;
    private String firstName;
    private String lastName;
    private long chatId;
    private Chat chat;

    public long getChatId() {
        return chatId;
    }
    public Chat getChat() {
        return chat;
    }
    public String getUsername() {
        return username;
    }
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}

    @Override
    public boolean equals(Object o) {
        try {
            var user = (User)o;
            return this.chatId == user.getChatId();
        } catch (ClassCastException e){
            return false;
        }
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this.getChatId());
    }
}
