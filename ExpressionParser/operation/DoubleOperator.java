package operation;

import exceptions.ImpossibleOperation;
import exceptions.NotAllowedOperation;

public class DoubleOperator implements Operator<Double> {
    @Override
    public Double add(Double first, Double second) {
        return first + second;
    }

    @Override
    public Double sub(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double div(Double first, Double second) throws NotAllowedOperation {
        return first / second;
    }

    @Override
    public Double max(Double first, Double second) {
        return first < second ? second : first;
    }

    @Override
    public Double min(Double first, Double second) {
        return first > second ? second : first;
    }

    @Override
    public Double parseFromInt(int first) {
        return (double) (first);
    }

    @Override
    public Double square(Double first) throws ImpossibleOperation {
        return mult(first, first);
    }

    @Override
    public Double mod(Double first, Double second) throws NotAllowedOperation {
        return first % second;
    }

    @Override
    public Double mult(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double not(Double first) {
        return -first;
    }

    @Override
    public Double abs(Double first) {
        return first >= 0 ? first : -first;
    }

    @Override
    public Double parse(String in) {
        return Double.parseDouble(in);
    }
}
