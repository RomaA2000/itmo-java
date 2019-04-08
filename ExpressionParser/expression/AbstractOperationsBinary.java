package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public abstract class AbstractOperationsBinary<T> implements TripleExpression<T> {
    Operator<T> operator;
    private TripleExpression<T> first, second;

    public AbstractOperationsBinary(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        first = left;
        second = right;
        operator = op;
    }

    abstract T calculate(T x, T y) throws ImpossibleOperation;

    public T evaluate(T x, T y, T z) throws ImpossibleOperation {
        return calculate(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }
}
