package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class CheckedMult<T> extends AbstractOperationsBinary<T> {
    public CheckedMult(TripleExpression<T> term1, TripleExpression<T> term2, Operator<T> op) {
        super(term1, term2, op);
    }

    T calculate(T x, T y) throws ImpossibleOperation {
        return operator.mult(x, y);
    }

}
