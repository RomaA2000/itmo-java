package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public void enqueue (Object element) {
        assert size >= 0;
        assert element != null;
        enqueueImplementation(element);
        size++;
    }

    public Object element () {
        assert size > 0;
        return elementImplementation();
    }

    public Object dequeue () {
        assert size > 0;
        Object answer = element();
        dequeueImplementation();
        size--;
        return answer;
    }

    public int size() {
        assert size >= 0;
        return size;
    }

    public boolean isEmpty() {
        assert size >= 0;
        return size == 0;
    }

    public void clear() {
        clearImplementation();
        size = 0;
    }

    private AbstractQueue copyQueue() {
        AbstractQueue answer = copyQueueImplementation();
        return answer;
    }

    public AbstractQueue filter (Predicate<Object> predicate) {
        AbstractQueue input = copyQueueImplementation();
        for (int i = 0; i < size; i++) {
            Object term = input.dequeue();
            if (predicate.test(term)) {
                input.enqueue(term);
            }
        }
        return input;
    }

    public AbstractQueue map (Function<Object, Object> function) {
        AbstractQueue input = copyQueueImplementation();
        for (int i = 0; i < size; i++) {
            input.enqueue(function.apply(input.dequeue()));
        }
        return input;
    }

    abstract AbstractQueue copyQueueImplementation();

    abstract void enqueueImplementation(Object element);

    abstract Object elementImplementation();

    abstract void dequeueImplementation();

    abstract void clearImplementation();

}
