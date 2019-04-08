package expression;

import exceptions.ImpossibleOperation;
import operation.Operator;

public class CheckedDivide<T> extends AbstractOperationsBinary<T> {
    public CheckedDivide(TripleExpression<T> left, TripleExpression<T> right, Operator<T> op) {
        super(left, right, op);
    }

    T calculate(T x, T y) throws ImpossibleOperation {
        return operator.div(x, y);
    }

}
