import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Highscore
{
    public static String generateTable(User user) {
        var highscores = getList();
        var builder = new StringBuilder();
        var linesToPrint = 10;
        if (highscores.size() < linesToPrint)
            linesToPrint = highscores.size();

        builder.append("Таблица рекордов:\n");
        for (var i = 0; i < linesToPrint; i++) {
            var usr = highscores.get(i);
            appendMedal(builder, i+1);
            appendUserInfo(builder, usr);
            builder.append(" - ").append(usr.getHighscore()).append("\n");
        }

        appendUserPlace(builder, user, highscores);
        return builder.toString();
    }

    private static List<User> getList()
    {
        return UserTable.get().stream()
                .filter((u) -> u.getHighscore() != 0)
                .sorted(Comparator.comparing(User::getHighscore).reversed())
                .collect(Collectors.toList());
    }

    private static void appendMedal(StringBuilder builder, int place)
    {
        switch (place) {
            case 1:
                builder.append("\uD83E\uDD47");
                break;
            case 2:
                builder.append("\uD83E\uDD48");
                break;
            case 3:
                builder.append("\uD83E\uDD49");
                break;
            default:
                builder.append(place).append(".");
        }
    }

    private static void appendUserInfo(StringBuilder builder, User usr)
    {
        builder.append(" ").append(usr.getFirstName());
        if (usr.getLastName() != null)
            builder.append(" ").append(usr.getLastName());
        if (usr.getUsername() != null)
            builder.append("(@").append(usr.getUsername()).append(")");
    }

    private static void appendUserPlace(StringBuilder builder, User user, List<User> highscores)
    {
        if (highscores.indexOf(user) == -1)
            builder.append("\nПока что у вас нет рекорда.");
        else {
            builder.append("\nВы на ")
                    .append(highscores.indexOf(user) + 1)
                    .append(" месте со счетом ")
                    .append(user.getHighscore())
                    .append(".");
        }
    }
}
