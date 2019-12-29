import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class KeyboardsStorage {
    private static final Keyboard mainMenuKeyboard = new ReplyKeyboardMarkup(
            new String[]{"/start", "/duel"},
            new String[]{"/help", "/top"},
            new String[]{"/stats", "/achievements"})
            .oneTimeKeyboard(true).resizeKeyboard(true).selective(true);
    private static final Keyboard numQuestionKeyboard = new ReplyKeyboardMarkup(
            new String[]{"1", "2"},
            new String[]{"3", "4"})
            .oneTimeKeyboard(true).resizeKeyboard(true).selective(true);
    private static final Keyboard adminMain = new ReplyKeyboardMarkup(
            new String[]{"/find", "/sendAll"},
            new String[]{"/sleep", "/exit"})
            .oneTimeKeyboard(true).resizeKeyboard(true).selective(true);
    private static final Keyboard adminFindDial = new ReplyKeyboardMarkup(
            new String[]{"/chatId", "/username"},
            new String[]{"/firstname", "/lastname"},
            new String[]{"/highscore", "/exit"})
            .oneTimeKeyboard(true).resizeKeyboard(true).selective(true);
    private static final Keyboard adminUserDial = new ReplyKeyboardMarkup(
            new String[]{"/talk", "/exit"})
            .oneTimeKeyboard(true).resizeKeyboard(true).selective(true);
    public static Keyboard get(Keyboards keyboard)
    {
        switch (keyboard)
        {
            case mainMenuKeyboard:
                return mainMenuKeyboard;
            case numQuestionKeyboard:
                return numQuestionKeyboard;
            case adminMain:
                return adminMain;
            case adminFindDial:
                return adminFindDial;
            case adminUserDial:
                return adminUserDial;
            default:
                return null;
        }
    }
}
