public class BotIOFactory {
    public static IO getBotIO(BotIOType botIOType) throws SerializationException {
        if (botIOType == BotIOType.Console) {
            return new ConsoleIO();
        } else if (botIOType == BotIOType.Telegram) {
            return new TelegramIO();
        }
        throw new IllegalStateException("Unexpected botType value: " + botIOType);
    }
}