package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class Sqr<T> extends AbstractOperationsUnary<T> {
    public Sqr(TripleExpression<T> left, Operator<T> operation) {
        super(left, operation);
    }

    T calculate(T x) throws ImpossibleOperation {
        return operator.square(x);
    }
}
