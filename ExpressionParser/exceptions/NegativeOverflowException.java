package exceptions;

public class NegativeOverflowException extends ImpossibleOperation {
    public NegativeOverflowException() {
        super("Negative overflow");
    }
}
