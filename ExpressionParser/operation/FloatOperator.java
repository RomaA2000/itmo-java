package operation;

import exceptions.ImpossibleOperation;
import exceptions.NotAllowedOperation;

public class FloatOperator implements Operator<Float> {

    @Override
    public Float add(Float first, Float second) {
        return first + second;
    }

    @Override
    public Float sub(Float first, Float second) {
        return first - second;
    }

    @Override
    public Float div(Float first, Float second) throws NotAllowedOperation {
        return first / second;
    }

    @Override
    public Float max(Float first, Float second) {
        return first < second ? second : first;
    }

    @Override
    public Float min(Float first, Float second) {
        return first > second ? second : first;
    }

    @Override
    public Float parseFromInt(int first) {
        return (float) (first);
    }

    @Override
    public Float square(Float first) throws ImpossibleOperation {
        return mult(first, first);
    }

    @Override
    public Float mod(Float first, Float second) throws NotAllowedOperation {
        return first % second;
    }

    @Override
    public Float mult(Float first, Float second) {
        return first * second;
    }

    @Override
    public Float not(Float first) {
        return -first;
    }

    @Override
    public Float abs(Float first) {
        return first >= 0 ? first : -first;
    }

    @Override
    public Float parse(String in) {
        return Float.parseFloat(in);
    }

}
