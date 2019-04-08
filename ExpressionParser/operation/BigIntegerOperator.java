package operation;

import exceptions.NotAllowedOperation;

import java.math.BigInteger;

public class BigIntegerOperator implements Operator<BigInteger> {
    @Override
    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger sub(BigInteger first, BigInteger second) {
        return first.subtract(second);
    }

    @Override
    public BigInteger abs(BigInteger first) {
        return first.abs();
    }

    @Override
    public BigInteger div(BigInteger first, BigInteger second) throws NotAllowedOperation {
        checkDiv(second);
        return first.divide(second);
    }

    void checkDiv(BigInteger second) throws NotAllowedOperation {
        if (second.equals(BigInteger.ZERO)) {
            throw new NotAllowedOperation("Division by zero");
        }
    }

    @Override
    public BigInteger max(BigInteger first, BigInteger second) {
        return first.max(second);
    }

    @Override
    public BigInteger min(BigInteger first, BigInteger second) {
        return first.min(second);
    }

    @Override
    public BigInteger parseFromInt(int first) {
        return new BigInteger(Integer.toString(first));
    }

    @Override
    public BigInteger square(BigInteger first) {
        return mult(first, first);
    }

    void checkMod(BigInteger second) throws NotAllowedOperation {
        if (second.equals(BigInteger.ZERO)) {
            throw new NotAllowedOperation("Mod by zero");
        }
    }

    @Override
    public BigInteger mod(BigInteger first, BigInteger second) throws NotAllowedOperation {
        checkMod(second);
        return first.mod(second);
    }

    @Override
    public BigInteger mult(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    @Override
    public BigInteger not(BigInteger first) {
        return first.not();
    }

    @Override
    public BigInteger parse(String in) {
        return new BigInteger(in);
    }
}
