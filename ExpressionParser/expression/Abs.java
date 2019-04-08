package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class Abs<T> extends AbstractOperationsUnary<T> {
    public Abs(TripleExpression<T> left, Operator<T> operation) {
        super(left, operation);
    }

    T calculate(T x) throws ImpossibleOperation {
        return operator.abs(x);
    }
}
