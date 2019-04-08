package exceptions;

public class ArgumentsParsingException extends ParsingException {
    public ArgumentsParsingException(String in, int position) {
        super("Forgotten argument", in, position);
    }
}
