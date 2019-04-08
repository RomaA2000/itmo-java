package exceptions;

public abstract class ImpossibleOperation extends Exception {
    ImpossibleOperation(String in) {
        super(in);
    }
}
