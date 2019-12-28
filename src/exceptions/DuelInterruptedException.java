public class DuelInterruptedException extends Exception{
    private int userNumber;
    private DuelInterruptedCause cause;
    public DuelInterruptedException(int userNumber, DuelInterruptedCause cause) {
        this.userNumber = userNumber;
        this.cause = cause;
    }
    public int getUserNumber(){return userNumber;}
    public DuelInterruptedCause getDuelCause(){return cause;}
}