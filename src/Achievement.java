import java.io.Serializable;

public class Achievement implements Serializable {

    public Achievement(String achievementText, String achievementDescription)
    {
        this.achievementText = achievementText;
        this.achievementDescription = achievementDescription;
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
}
