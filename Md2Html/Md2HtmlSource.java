package md2html;

public abstract class Md2HtmlSource {
    public static final char END = '\0';
    protected int pos;
    private char c;

    protected abstract void write(String in);

    protected abstract char readChar();

    public char getChar() {
        return c;
    }

    public char nextChar() {
        c = readChar();
        pos++;
        return c;
    }

}
