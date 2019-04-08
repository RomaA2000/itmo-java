package expression;

public class Const<T> implements TripleExpression<T> {
    private T value;

    public Const(T term) {
        value = term;
    }

    public T evaluate(T term1, T term2, T term3) {
        return value;
    }
}
