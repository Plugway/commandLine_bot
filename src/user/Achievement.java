import java.io.Serializable;

public class Achievement implements Serializable {

    public Achievement(String achievementText, String achievementDescription, User user)
    {
        this.achievementText = achievementText;
        this.achievementDescription = achievementDescription;
        if (!user.getAchievements().contains(this))
            user.addAchievement(this);
    }
    private String achievementText;
    private String achievementDescription;
    public String newToString()
    {
        return "\uD83C\uDF89Разблокировано достижение\uD83C\uDF89\n"+this.toString();
    }
    public String toString()
    {
        return achievementText+"\n"+achievementDescription;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj.getClass() == this.getClass()) {
            var a =(Achievement)obj;
            return a.achievementText.equals(this.achievementText) && a.achievementDescription.equals(this.achievementDescription);
        }
        return false;
    }
}
