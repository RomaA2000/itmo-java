package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class CheckedNot<T> extends AbstractOperationsUnary<T> {

    public CheckedNot(TripleExpression<T> term, Operator<T> op) {
        super(term, op);
    }

    T calculate(T x) throws ImpossibleOperation {
        return operator.not(x);
    }
}
