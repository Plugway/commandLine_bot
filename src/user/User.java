import com.pengrad.telegrambot.model.Chat;

import java.io.Serializable;
import java.util.ArrayList;
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
        playsDuel = false;
        currentQuestCount = 0;
    }

    public Queue<String> messages = new PriorityQueue<>();
    private String username;
    private String firstName;
    private String lastName;
    private long chatId;
    private boolean playsDuel;
    private int currentQuestCount;
    private Stats stats = new Stats();
    private List<Achievement> achievements = new ArrayList<>();

    public long getChatId() {
        return chatId;
    }

    public Stats getStats(){return stats;}

    public void addAchievement(Achievement achievement){achievements.add(achievement);}

    public int getHighscore(){return stats.getHighscore();}

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean playsDuel(){ return playsDuel; }

    public void togglePlaysDuel(){playsDuel = !playsDuel;}

    public void setCurrentQuestCount(int currentQuestCount){this.currentQuestCount = currentQuestCount;}

    public int getCurrentQuestCount(){return currentQuestCount;}

    public List<Achievement> getAchievements(){return achievements;}

    public String getAchievementsListToPrint()
    {
        if (achievements.size() == 0)
            return "У вас пока что нет достижений.";
        var sb = new StringBuilder();
        for (var i = 0; i < achievements.size(); i++)
        {
            sb.append(i + 1).append(". ").append(achievements.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

    public String getDetalizedToPrint()
    {
        return "Информация:\n" +
                "Firstname: " + this.firstName + "\n" +
                "Lastname: " + this.lastName + "\n" +
                "Username: @" + this.username + "\n" +
                "ChatId: " + this.chatId + "\n" +
                "Highscore: " + this.stats.getHighscore();
    }
    public String getToPrint()
    {
        var builder = new StringBuilder();
        builder.append(this.firstName);
        if (this.lastName != null)
            builder.append(" ").append(this.lastName);
        if (this.username != null)
            builder.append("(@").append(this.username).append(")");
        builder.append(" - ").append(this.stats.getHighscore());
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