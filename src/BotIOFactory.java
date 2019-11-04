public class BotIOFactory
{
    public static IO getBotIO(BotIOType botIOType)
    {
        switch (botIOType) {
            case Console:
                return new ConsoleIO();
            case Telegram:
                return new TelegramIO();
            default:
                throw new IllegalStateException("Unexpected botType value: " + botIOType);
        }
    }
}
