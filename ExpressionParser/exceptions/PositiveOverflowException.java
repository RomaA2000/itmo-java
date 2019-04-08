package exceptions;

public class PositiveOverflowException extends ImpossibleOperation {
    public PositiveOverflowException() {
        super("Positive overflow");
    }
}
