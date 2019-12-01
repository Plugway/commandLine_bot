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
            builder.append(printPlace(i+1)).append(usr.getToPrint()).append("\n");
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

    private static String printPlace(int place)
    {
        switch (place) {
            case 1:
                return "\uD83E\uDD47";
            case 2:
                return "\uD83E\uDD48";
            case 3:
                return "\uD83E\uDD49";
            default:
                return place + ".";
        }
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
