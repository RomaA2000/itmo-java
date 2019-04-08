package operation;

import exceptions.ImpossibleOperation;
import exceptions.NegativeOverflowException;
import exceptions.NotAllowedOperation;
import exceptions.PositiveOverflowException;

public class IntegerOperator implements Operator<Integer> {
    private final boolean check;

    public IntegerOperator(final boolean flag) {
        check = flag;
    }

    private void addCheck(int x, int y) throws ImpossibleOperation {
        if ((x > 0) && (Integer.MAX_VALUE - x < y)) {
            throw new PositiveOverflowException();
        }
        if ((x < 0) && (Integer.MIN_VALUE - x > y)) {
            throw new NegativeOverflowException();
        }
    }

    @Override
    public Integer add(Integer first, Integer second) throws ImpossibleOperation {
        if (check) {
            addCheck(first, second);
        }
        return first + second;
    }

    private void subCheck(int x, int y) throws ImpossibleOperation {
        if ((x >= 0) && (y <= 0) && (x > y + Integer.MAX_VALUE)) {
            throw new PositiveOverflowException();
        }
        if ((x <= 0) && (y >= 0) && (Integer.MIN_VALUE + y > x)) {
            throw new NegativeOverflowException();
        }
    }

    @Override
    public Integer sub(Integer first, Integer second) throws ImpossibleOperation {
        if (check) {
            subCheck(first, second);
        }
        return first - second;
    }

    private void checkDiv(Integer second) throws NotAllowedOperation {
        if (second == 0) {
            throw new NotAllowedOperation("Division by zero");
        }
    }

    @Override
    public Integer div(Integer first, Integer second) throws NotAllowedOperation {
        checkDiv(second);
        return first / second;
    }

    @Override
    public Integer max(Integer first, Integer second) {
        return first < second ? second : first;
    }

    @Override
    public Integer min(Integer first, Integer second) {
        return first > second ? second : first;
    }

    @Override
    public Integer parseFromInt(int first) {
        return first;
    }

    @Override
    public Integer square(Integer first) throws ImpossibleOperation {
        return mult(first, first);
    }

    private void checkMod(Integer second) throws NotAllowedOperation {
        if (second.equals(0)) {
            throw new NotAllowedOperation("Mod by zero");
        }
    }

    @Override
    public Integer mod(Integer first, Integer second) throws NotAllowedOperation {
        if (check) {
            checkMod(second);
        }
        return first % second;
    }

    private void multCheck(int x, int y) throws ImpossibleOperation {
        if ((x > 0) && (y > 0) && (Integer.MAX_VALUE / x < y)) {
            throw new PositiveOverflowException();
        }
        if ((x < 0) && (y < 0) && (Integer.MAX_VALUE / x > y)) {
            throw new PositiveOverflowException();
        }
        if ((x < 0) && (y > 0) && (Integer.MIN_VALUE / y > x)) {
            throw new NegativeOverflowException();
        }
        if ((x > 0) && (y < 0) && (Integer.MIN_VALUE / x > y)) {
            throw new NegativeOverflowException();
        }
    }

    @Override
    public Integer mult(Integer first, Integer second) throws ImpossibleOperation {
        if (check) {
            multCheck(first, second);
        }
        return first * second;
    }

    private void notCheck(int x) throws ImpossibleOperation {
        if (x == Integer.MIN_VALUE) {
            throw new PositiveOverflowException();
        }
    }

    @Override
    public Integer not(Integer first) throws ImpossibleOperation {
        if (check) {
            notCheck(first);
        }
        return -first;
    }


    private void absCheck(int x) throws ImpossibleOperation {
        if (x == Integer.MIN_VALUE) {
            throw new PositiveOverflowException();
        }
    }

    @Override
    public Integer abs(Integer first) throws ImpossibleOperation {
        if (check) {
            absCheck(first);
        }
        return first > 0 ? first : -first;
    }

    @Override
    public Integer parse(String in) {
        return Integer.parseInt(in);
    }
}
