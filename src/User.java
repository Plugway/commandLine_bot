import com.pengrad.telegrambot.model.Chat;

import java.io.Serializable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class User implements Serializable {
    public User(Chat chat) {
        username = chat.username();
        firstName = chat.firstName();
        lastName = chat.lastName();
        chatId = chat.id();
        this.chat = chat;
        duelId = 0;
        highscore = 0;
        duelQuestCount = 0;
    }

    public Queue<String> messages = new PriorityQueue<>();
    private String username;
    private String firstName;
    private String lastName;
    private long chatId;
    private Chat chat;
    private int highscore;
    private long duelId;
    private int duelQuestCount;

    public long getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public int getHighscore() {
        return highscore;
    }

    public long getDuelId(){ return duelId; }

    public void setDuelId(long duelId){this.duelId = duelId;}

    public void setDuelQuestCount(int duelQuestCount){this.duelQuestCount = duelQuestCount;}

    public int getDuelQuestCount(){return duelQuestCount;}

    public String getDetalizedToPrint()
    {
        return "Информация:\n" +
                "Firstname: " + this.firstName + "\n" +
                "Lastname: " + this.lastName + "\n" +
                "Username: @" + this.username + "\n" +
                "ChatId: " + this.chatId + "\n" +
                "Highscore: " + this.highscore;
    }
    public String getToPrint()
    {
        var builder = new StringBuilder();
        builder.append(this.firstName);
        if (this.lastName != null)
            builder.append(" ").append(this.lastName);
        if (this.username != null)
            builder.append("(@").append(this.username).append(")");
        builder.append(" - ").append(this.highscore);
        return builder.toString();
    }
    public static String getListToPrint(List<User> users){
        return "Найдено:\n"+
                users.stream()
                        .map(u -> (users.indexOf(u)+1)+". "+u.getToPrint())
                        .collect(Collectors.joining("\n"));
    }
    @Override
    public boolean equals(Object o) {
        try {
            var user = (User) o;
            return this.chatId == user.getChatId();
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this.getChatId());
    }
}