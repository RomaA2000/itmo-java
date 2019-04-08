package exceptions;

public class SymbolParsingException extends ParsingException {
    public SymbolParsingException(String in, int position) {
        super("Invalid character", in, position);
    }
}
