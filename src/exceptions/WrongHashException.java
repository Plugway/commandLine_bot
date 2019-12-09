public class WrongHashException extends Exception
{
    public WrongHashException() {}

    public WrongHashException(String message) {
        super(message);
    }
}