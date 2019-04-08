package expression;

import operation.Operator;

public class CheckedMin<T> extends AbstractOperationsBinary<T> {
    public CheckedMin(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    T calculate(T x, T y) {
        return operator.min(x, y);
    }
}
