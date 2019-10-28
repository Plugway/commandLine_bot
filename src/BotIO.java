public class BotIO
{
    private static IO botIO;
    public static IO getBotIO() { return botIO; }   // i dislike this solution, discuss if wish to propose a better one

    public static void selectIOClass()
    {
        switch (Main.botMode)
        {
            case Console:
                botIO = new ConsoleIO();
                break;
            case Telegram:
                botIO = new TelegramIO();
                break;
            default:
                throw new IllegalStateException("Unexpected botIO value: " + Main.botMode);
        }
    }

}
