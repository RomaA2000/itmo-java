package splitter;

import exceptions.*;
import operation.Operator;

import java.util.ArrayList;

public class Splitter<T> implements StringSplitter<T> {
    private static final String[] ALL_OPERATIONS = {"min", "max", "abs", "square", "mod"};
    private String in;
    private Element nowElement;
    private Operator<T> operations;
    private T constValue;
    private String variableName;
    private ArrayList<Trit> expression;

    public Splitter(String input, Operator<T> op) throws ParsingException {
        expression = new ArrayList<>();
        operations = op;
        in = input;
        int position = 0;
        nowElement = Element.ERROR;
        position = nextOperation(position);
        for (; nowElement != Element.THE_END; position = nextOperation(position)) {
            Trit operation = new Trit();
            if (nowElement == Element.VARIABLE) {
                operation.variable = variableName;
            } else {
                operation.variable = null;
            }
            if (nowElement == Element.NUMBER) {
                operation.constant = constValue;
            } else {
                operation.constant = null;
            }
            operation.term = nowElement;
            operation.index = position;
            expression.add(operation);
        }
        check();
    }

    public Element thisElement(int iterator) {
        if (iterator >= expression.size()) {
            return Element.THE_END;
        } else {
            return expression.get(iterator).term;
        }
    }

    public T thisValue(int iterator) {
        if (iterator >= expression.size()) {
            return null;
        } else {
            return expression.get(iterator).constant;
        }
    }

    public String thisVariable(int iterator) {
        if (iterator >= expression.size()) {
            return "";
        } else {
            return expression.get(iterator).variable;
        }
    }

    private int nextOperation(int position) throws ParsingException {
        position = skip(position);
        if (position >= in.length()) {
            nowElement = Element.THE_END;
        } else {
            char el = in.charAt(position);
            switch (el) {
                case '(':
                    nowElement = Element.BRACE_OPEN;
                    break;
                case ')':
                    nowElement = Element.BRACE_CLOSE;
                    break;
                case '-':
                    if ((nowElement == Element.BRACE_CLOSE) || (nowElement == Element.VARIABLE) || (nowElement == Element.NUMBER)) {
                        nowElement = Element.SUB;
                    } else {
                        nowElement = Element.NOT;
                        if ((position + 1 < in.length()) && (Character.isDigit(in.charAt(position + 1)))) {
                            int left = position++;
                            while ((position < in.length()) && (Character.isDigit(in.charAt(position)))) {
                                position++;
                            }
                            int right = position;
                            try {
                                constValue = operations.parse(in.substring(left, right));
                            } catch (Exception e) {
                                throw new InvalidConstantException(in, position);
                            }
                            nowElement = Element.NUMBER;
                            position--;
                        }
                    }
                    break;
                case '*':
                    nowElement = Element.MULT;
                    break;
                case '/':
                    nowElement = Element.DIV;
                    break;
                case '+':
                    nowElement = Element.ADD;
                    break;
                case 'x':
                    nowElement = Element.VARIABLE;
                    variableName = "x";
                    break;
                case 'y':
                    nowElement = Element.VARIABLE;
                    variableName = "y";
                    break;
                case 'z':
                    nowElement = Element.VARIABLE;
                    variableName = "z";
                    break;
                default:
                    if (Character.isDigit(el)) {
                        int left = position;
                        while ((position < in.length()) && (Character.isDigit(in.charAt(position)))) {
                            position++;
                        }
                        int right = position;
                        try {
                            constValue = operations.parse(in.substring(left, right));
                        } catch (Exception e) {
                            throw new InvalidConstantException(in, position);
                        }
                        nowElement = Element.NUMBER;
                        position--;
                    } else {
                        boolean found = false;
                        for (String allOperation : ALL_OPERATIONS) {
                            if ((position + allOperation.length() < in.length()) &&
                                    (isGoodSymbol(in.charAt(position + allOperation.length()))) &&
                                    (in.substring(position, position + allOperation.length()).equals(allOperation))) {
                                position += allOperation.length() - 1;
                                found = true;
                                nowElement = elementFromString(allOperation);
                                break;
                            } else if ((position + allOperation.length() == in.length())
                                    && (in.substring(position, position + allOperation.length()).equals(allOperation))) {
                                throw new ArgumentsParsingException(in, position);
                            }
                        }
                        if (!found) {
                            nowElement = Element.ERROR;
                        }
                    }
            }
            position++;
        }
        return position;
    }

    private Element elementFromString(String in) {
        if (in.equals("abs")) {
            return Element.ABS;
        }
        if (in.equals("square")) {
            return Element.SQR;
        }
        if (in.equals("min")) {
            return Element.MIN;
        }
        if (in.equals("max")) {
            return Element.MAX;
        }
        if (in.equals("mod")) {
            return Element.MOD;
        }
        return Element.ERROR;
    }

    private boolean isGoodSymbol(char in) {
        return !((Character.isLetter(in)) || (Character.isDigit(in)));
    }

    private boolean isRightOperand(Element in) {
        return (in == Element.BRACE_CLOSE)
                || (in == Element.VARIABLE)
                || (in == Element.NUMBER);
    }

    private boolean isLeftOperand(Element in) {
        return (in == Element.BRACE_OPEN)
                || (in == Element.VARIABLE)
                || (in == Element.NUMBER)
                || (isUnaryOperation(in));
    }

    private boolean isBinaryOperation(Element in) {
        return (in == Element.ADD) || (in == Element.SUB) || (in == Element.MULT)
                || (in == Element.DIV) || (in == Element.MIN) || (in == Element.MAX) || (in == Element.MOD);
    }

    private boolean isUnaryOperation(Element in) {
        return (in == Element.NOT) || (in == Element.SQR) || (in == Element.ABS);
    }

    private boolean isLeftOperation(Element in) {
        return (isBinaryOperation(in)) || (isUnaryOperation(in)) || (in == Element.BRACE_OPEN);
    }

    private boolean isRightOperation(Element in) {
        return (isBinaryOperation(in)) || (in == Element.BRACE_CLOSE);
    }

    private int skip(int position) {
        while ((position < in.length()) && (Character.isWhitespace(in.charAt(position)))) {
            position++;
        }
        return position;
    }


    private class Trit {
        Element term;
        String variable;
        int index;
        T constant;
    }

    private void check() throws ParsingException {
        int size = expression.size();
        int balance = 0;
        Element now;
        int position = 0;
        for (; position < size; position++) {
            now = expression.get(position).term;
            if (now == Element.ERROR) {
                throw new SymbolParsingException(in, expression.get(position).index);
            }
        }
        position = 0;
        for (; position < size; position++) {
            now = expression.get(position).term;
            if (now == Element.BRACE_OPEN) {
                balance++;
            }
            if (now == Element.BRACE_CLOSE) {
                balance--;
            }
            if ((isUnaryOperation(now)) && ((position == size - 1)
                    || !(isLeftOperand(expression.get(position + 1).term)))) {
                throw new ArgumentsParsingException(in, expression.get(position).index);
            }
            if ((isBinaryOperation(now)) && ((position == size - 1) || (position == 0)
                    || !(isRightOperand(expression.get(position - 1).term))
                    || !(isLeftOperand(expression.get(position + 1).term)))) {
                throw new ArgumentsParsingException(in, expression.get(position).index);
            }
            if (((now == Element.NUMBER) || (now == Element.VARIABLE))
                    && ((!(position == 0) && !(isLeftOperation(expression.get(position - 1).term)))
                    || (!(position == size - 1) && !(isRightOperation(expression.get(position + 1).term))))) {
                throw new ArgumentsParsingException(in, expression.get(position).index);
            }
            if ((now == Element.BRACE_OPEN) && ((position == size - 1) || !(isLeftOperand(expression.get(position + 1).term)))) {
                throw new ArgumentsParsingException(in, expression.get(position).index);
            }
            if ((now == Element.BRACE_CLOSE) && ((position == 0) || !(isRightOperand(expression.get(position - 1).term)))) {
                throw new ArgumentsParsingException(in, expression.get(position).index);
            }
            if (balance < 0) {
                throw new BraceParsingException(in, expression.get(position).index);
            }
        }
        if (balance != 0) {
            throw new BraceParsingException(in, expression.get(position - 1).index);
        }
    }
}
