package parser;

import exceptions.ParsingException;
import expression.TripleExpression;
import operation.Operator;

public interface Parser<T> {
    TripleExpression<T> parse(String expression, Operator<T> operation) throws ParsingException;
}
