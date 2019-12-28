public class BotIOFactory {
    public static IO getBotIO(BotIOType botIOType) {
        if (botIOType == BotIOType.Console) {
            return new ConsoleIO();
        } else if (botIOType == BotIOType.Telegram) {
            return new TelegramIO();
        }
        Logger.log(LogLevels.fatal, "BotIOFactory: Unexpected botType value " + botIOType);
        throw new IllegalStateException("Unexpected botType value: " + botIOType);
    }
}