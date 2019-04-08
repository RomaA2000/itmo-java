package exceptions;

public class BraceParsingException extends ParsingException {
    public BraceParsingException(String in, int position) {
        super("Forgotten Brace", in, position);
    }
}
