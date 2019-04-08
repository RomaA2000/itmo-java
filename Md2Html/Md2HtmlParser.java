package md2html;

public class Md2HtmlParser {
    private final Md2HtmlSource source;
    private Condition now;
    private boolean wsFlag;

    public Md2HtmlParser(final Md2HtmlSource source) {
        this.source = source;
        now = Condition.NOTHING;
        wsFlag = false;
    }

    public void parse() {
        source.nextChar();
        skipIndent();
        now = Condition.NOTHING;
        String in = parseValue(Md2HtmlSource.END).toString();
        source.write(in);
    }

    private StringBuilder parseValue(final char breakPoint) {
        StringBuilder result = new StringBuilder();
        while (!testChar(breakPoint)) {
            switch (source.getChar()) {
                case '#':
                    result.append(parseLattice());
                    break;
                case '*':
                case '_':
                    result.append(parseAccentuation());
                    break;
                case '-':
                    result.append(parseDash());
                    break;
                case '`':
                    result.append(parseComma());
                    break;
                case '\\':
                    result.append(parseBackslash());
                    break;
                default:
                    if (now == Condition.NOTHING) {
                        result.append(parseText());
                        checkWS();
                    } else {
                        result.append(parseSymbol()).append(getIndent());
                    }
            }
            if (isEnd()) {
                return result;
            }
        }
        return result;
    }

    private StringBuilder parseSymbol() {
        StringBuilder result = new StringBuilder();
        switch (source.getChar()) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '!':
                result.append(parseExclamationMark());
                break;
            default:
                result.append(source.getChar());
        }
        return result;
    }

    private StringBuilder parseExclamationMark() {
        skip(2);
        StringBuilder result = new StringBuilder();
        result.append("<img alt='");
        while (!testChar(']')) {
            result.append(source.getChar());
            update();
        }
        skip(2);
        result.append("' src='");
        while (!testChar(')')) {
            result.append(source.getChar());
            update();
        }
        result.append("'>");
        return result;
    }

    private StringBuilder parseAccentuation() {
        char c = source.getChar();
        checkWS();
        StringBuilder result = new StringBuilder();
        update();
        if (testChar(c)) {
            if (!isWS()) {
                result.append(parseDoubleAccentuation(c));
            } else {
                result.append(c).append(c);
            }
        } else if (!isWS()) {
            result.append(parseSingleAccentuation(c));
        } else {
            result.append(c);
            result.append(source.getChar());
        }
        update();
        return result;
    }

    private StringBuilder parseSingleAccentuation(char c) {
        StringBuilder result = new StringBuilder();
        now = Condition.TEXT;
        result.append("<em>");
        do {
            result.append(parseValue(c));
            if (wsFlag) {
                result.append(c);
                update();
            }
        } while (wsFlag);
        result.append("</em>");
        return result;
    }

    private StringBuilder parseDoubleAccentuation(char c) {
        StringBuilder result = new StringBuilder();
        now = Condition.TEXT;
        result.append("<strong>");
        update();
        boolean cycleNotEnd;
        do {
            result.append(parseValue(c));
            cycleNotEnd = (checkWS()) && (testChar(c));
            if (cycleNotEnd) {
                result.append(c);
                update();
            }
        } while (cycleNotEnd);
        result.append("</strong>");
        update();
        return result;
    }


    private StringBuilder parseDash() {
        StringBuilder result = new StringBuilder();
        update();
        if (testChar('-')) {
            if (!isWS()) {
                result.append("<s>");
                now = Condition.TEXT;
                do {
                    result.append(parseValue('-'));
                    update();
                } while (!testChar('-'));
                result.append("</s>");
                update();
                return result;
            }
            return new StringBuilder("--");
        }
        return new StringBuilder("-");
    }

    private StringBuilder parseComma() {
        StringBuilder result = new StringBuilder();
        update();
        now = Condition.TEXT;
        if (!isWS()) {
            result.append("<code>").append(parseSymbol());
            update();
            result.append(parseValue('`')).append("</code>");
            update();
            return result;
        }
        return new StringBuilder("'");
    }

    private StringBuilder parseText(StringBuilder in) {
        StringBuilder result = new StringBuilder();
        now = Condition.TEXT;
        result.append("<p>").append(in).append(parseWhile()).append("</p>");
        if (!isEnd()) {
            result.append("\n");
        }
        skipIndent();
        now = Condition.NOTHING;
        return result;
    }

    private StringBuilder parseBackslash() {
        update();
        StringBuilder result = new StringBuilder();
        switch (source.getChar()) {
            case '*':
                result.append("*");
                break;
            case '_':
                result.append("_");
                break;
            default:
                result.append("\\");
        }
        update();
        return result;
    }

    private StringBuilder parseLattice() {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        while (testChar('#')) {
            counter++;
            source.nextChar();
        }
        if ((now == Condition.NOTHING) && (isWS())) {
            now = Condition.HEADER;
            result.append("<h").append(String.valueOf(counter)).append(">");
            source.nextChar();
            result.append(parseWhile());
            skipIndent();
            result.append("</h").append(String.valueOf(counter)).append(">");
            if (!isEnd()) {
                result.append("\n");
            }
            now = Condition.NOTHING;
        } else {
            if (!(now == Condition.NOTHING)) {
                result.append(genStringBuilder(counter, '#'));
            } else {
                result.append(parseText(genStringBuilder(counter, '#')));
            }
        }
        return result;
    }



    private StringBuilder parseWhile() {
        StringBuilder result = new StringBuilder();
        do {
            result.append(parseValue('\n'));
            update();
            if (!testChar('\n') && !(isEnd())) {
                result.append('\n');
            }
        } while (!testChar('\n') && !(isEnd()));
        return result;
    }

    private StringBuilder parseText() {
        return parseText(new StringBuilder());
    }

    private void update() {
        checkWS();
        source.nextChar();
    }

    private void skipIndent() {
        while (testChar('\n')) {
            source.nextChar();
        }
    }

    private StringBuilder getIndent() {
        char nowChar, next;
        nowChar = source.getChar();
        update();
        next = source.getChar();
        if ((nowChar == next) && (nowChar == '\n') && (now == Condition.NOTHING)) {
            while (testChar('\n')) {
                source.nextChar();
            }
            return new StringBuilder("\n\n");
        }
        return new StringBuilder();
    }

    private StringBuilder genStringBuilder(int number, char symbol) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < number; i++) {
            out.append(symbol);
        }
        return out;
    }

    private void skip(int n) {
        for (int i = 0; i < n; i++) {
            update();
        }
    }

    private boolean isWS() {
        return Character.isWhitespace(source.getChar());
    }

    private boolean checkWS() {
        wsFlag = Character.isWhitespace(source.getChar());
        return wsFlag;
    }

    private boolean testChar(char in) {
        return source.getChar() == in;
    }

    private boolean isEnd() {
        return source.getChar() == Md2HtmlSource.END;
    }

    private enum Condition {NOTHING, TEXT, HEADER}
}
