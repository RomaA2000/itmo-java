package operation;

import exceptions.ImpossibleOperation;
import exceptions.NotAllowedOperation;

public class ByteOperator implements Operator<Byte> {
    @Override
    public Byte add(Byte first, Byte second) throws ImpossibleOperation {
        return (byte) (first + second);
    }

    @Override
    public Byte sub(Byte first, Byte second) throws ImpossibleOperation {
        return (byte) (first - second);
    }


    private void checkDiv(Byte second) throws NotAllowedOperation {
        if (second == 0) {
            throw new NotAllowedOperation("Division by zero");
        }
    }

    @Override
    public Byte div(Byte first, Byte second) throws NotAllowedOperation {
        checkDiv(second);
        return (byte) (first / second);
    }

    @Override
    public Byte max(Byte first, Byte second) {
        return (byte) first < second ? second : first;
    }

    @Override
    public Byte min(Byte first, Byte second) {
        return (byte) first > second ? second : first;
    }

    @Override
    public Byte parseFromInt(int first) {
        return (byte) (int) first;
    }

    @Override
    public Byte square(Byte first) throws ImpossibleOperation {
        return mult(first, first);
    }


    private void checkMod(Byte second) throws NotAllowedOperation {
        if (second == 0) {
            throw new NotAllowedOperation("Mode by zero");
        }
    }

    @Override
    public Byte mod(Byte first, Byte second) throws NotAllowedOperation {
        checkMod(second);
        return (byte) (first % second);
    }

    @Override
    public Byte mult(Byte first, Byte second) throws ImpossibleOperation {
        return (byte) (first * second);
    }

    @Override
    public Byte not(Byte first) throws ImpossibleOperation {
        return (byte) -first;
    }

    @Override
    public Byte abs(Byte first) throws ImpossibleOperation {
        return (byte) (first > 0 ? first : -first);
    }

    @Override
    public Byte parse(String in) {
        return Byte.parseByte(in);
    }
}
