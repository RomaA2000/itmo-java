package exceptions;

public class UnknownModeException extends Exception {
    public UnknownModeException(String in) {
        super("Unknown mode : " + in);
    }
}
