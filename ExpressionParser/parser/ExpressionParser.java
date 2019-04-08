package parser;

import exceptions.ParsingException;
import expression.*;
import operation.Operator;
import splitter.Splitter;
import splitter.StringSplitter;
import splitter.StringSplitter.Element;

public class ExpressionParser<T> implements Parser<T> {
    private int iterator;
    private StringSplitter<T> expression;
    private Element nowElement;
    private Operator<T> nowOperator;

    public TripleExpression<T> parse(String in, Operator<T> op) throws ParsingException {
        expression = new Splitter<>(in, op);
        nowOperator = op;
        nowElement = Element.ERROR;
        iterator = 0;
        return minMax();
    }

    private TripleExpression<T> minMax() {
        TripleExpression<T> result = add();
        while (true) {
            switch (nowElement) {
                case MAX:
                    result = new CheckedMax<>(result, add(), nowOperator);
                    break;
                case MIN:
                    result = new CheckedMin<>(result, add(), nowOperator);
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> add() {
        TripleExpression<T> result = multiply();
        while (true) {
            switch (nowElement) {
                case ADD:
                    result = new CheckedAdd<>(result, multiply(), nowOperator);
                    break;
                case SUB:
                    result = new CheckedSub<>(result, multiply(), nowOperator);
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> multiply() {
        TripleExpression<T> result = unary();
        while (true) {
            switch (nowElement) {
                case MULT:
                    result = new CheckedMult<>(result, unary(), nowOperator);
                    break;
                case MOD:
                    result = new Mod<>(result, unary(), nowOperator);
                    break;
                case DIV:
                    result = new CheckedDivide<>(result, unary(), nowOperator);
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> unary() {
        nowElement = expression.thisElement(iterator);
        iterator++;
        TripleExpression<T> result = null;
        switch (nowElement) {
            case BRACE_CLOSE:
                nowElement = expression.thisElement(iterator);
                iterator++;
                break;
            case BRACE_OPEN:
                result = minMax();
                nowElement = expression.thisElement(iterator);
                iterator++;
                break;
            case VARIABLE:
                result = new Variable<>(expression.thisVariable(iterator - 1));
                nowElement = expression.thisElement(iterator);
                iterator++;
                break;
            case NUMBER:
                result = new Const<>(expression.thisValue(iterator - 1));
                nowElement = expression.thisElement(iterator);
                iterator++;
                break;
            case NOT:
                result = new CheckedNot<>(unary(), nowOperator);
                break;
            case ABS:
                result = new Abs<>(unary(), nowOperator);
                break;
            case SQR:
                result = new Sqr<>(unary(), nowOperator);
                break;
            default:
                return null;
        }
        return result;
    }
}
