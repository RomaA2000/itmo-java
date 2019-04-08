package exceptions;

public class InvalidConstantException extends ParsingException {
    public InvalidConstantException(String in, int position) {
        super("Invalid constant", in, position);
    }
}
