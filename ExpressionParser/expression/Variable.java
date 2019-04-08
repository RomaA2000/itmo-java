package expression;

public class Variable<T> implements TripleExpression<T> {
    private String variable;

    public Variable(String name) {
        variable = name;
    }

    public T evaluate(T term1, T term2, T term3) {
        switch (variable) {
            case "x":
                return term1;
            case "y":
                return term2;
            case "z":
                return term3;
        }
        return null;
    }
}
