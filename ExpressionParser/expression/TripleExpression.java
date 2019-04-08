package expression;

import exceptions.ImpossibleOperation;

public interface TripleExpression<T> {
    T evaluate(T term1, T term2, T term3) throws ImpossibleOperation;
}
