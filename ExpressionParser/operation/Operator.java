package operation;

import exceptions.ImpossibleOperation;
import exceptions.NotAllowedOperation;

public interface Operator<T> {

    T add(final T first, final T second) throws ImpossibleOperation;

    T sub(final T first, final T second) throws ImpossibleOperation;

    T div(final T first, final T second) throws NotAllowedOperation;

    T max(final T first, final T second);

    T min(final T first, final T second);

    T parseFromInt(int first);

    T square(final T first) throws ImpossibleOperation;

    T mod(final T first, T second) throws NotAllowedOperation;

    T mult(final T first, final T second) throws ImpossibleOperation;

    T not(final T first) throws ImpossibleOperation;

    T abs(final T first) throws ImpossibleOperation;

    T parse(final String in);
}
