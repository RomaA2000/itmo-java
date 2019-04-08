package splitter;

public interface StringSplitter<T> {

    T thisValue(int iterator);

    String thisVariable(int iterator);

    Element thisElement(int iterator);

    enum Element {BRACE_OPEN, BRACE_CLOSE, VARIABLE, NUMBER, ABS, SQR, NOT, MULT, DIV, ADD, SUB, MIN, MAX, THE_END, ERROR, MOD}
}
