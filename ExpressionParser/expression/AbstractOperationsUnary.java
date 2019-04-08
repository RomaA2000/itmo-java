package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public abstract class AbstractOperationsUnary<T> implements TripleExpression<T> {
    private TripleExpression<T> first;
    Operator<T> operator;

    public AbstractOperationsUnary(TripleExpression<T> left, Operator<T> op) {
        first = left;
        operator = op;
    }

    abstract T calculate(T x) throws ImpossibleOperation;

    public T evaluate(T x, T y, T z) throws ImpossibleOperation {
        return calculate(first.evaluate(x, y, z));
    }
}

