package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class CheckedMax<T> extends AbstractOperationsBinary<T> {

    public CheckedMax(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    T calculate(T x, T y) throws ImpossibleOperation {
        return operator.max(x, y);
    }
}
