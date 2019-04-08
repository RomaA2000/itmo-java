package queue;

public class ArrayQueue extends AbstractQueue{
    private final int START_SIZE = 16;
    private Object[] array = new Object[START_SIZE];
    private int left = 0;
    private int right = 0;

    public ArrayQueue () {
        array = new Object[START_SIZE];
    }

    private ArrayQueue(int length) {
        array = new Object[length];
    }

    private void ensureQueue(int length) {
        if (array.length <= length) {
            Object[] buffer;
            if (length == 0) {
                buffer = new Object[START_SIZE];
            } else {
                buffer = new Object[length * 2];
            }
            int k = 0;
            int i = left;
            while (i != right) {
                buffer[k] = array[i];
                k++;
                i = next(i);
            }
            array = buffer;
            left = 0;
            right = k;
        }
    }

    void enqueueImplementation(Object element) {
        ensureQueue(size + 1);
        array[right] = element;
        right = next(right);
    }

    Object elementImplementation() {
        return array[left];
    }

    void dequeueImplementation() {
        array[left] = null;
        left = next(left);
    }

    void clearImplementation() {
        left = 0;
        right = 0;
    }

    private int next(int x) {
        return (x + 1) % array.length;
    }

    private int pre(int x) {
        if (x != 0) {
            return x - 1;
        } else {
            return array.length - 1;
        }
    }

    ArrayQueue copyQueueImplementation() {
        ArrayQueue answer = new ArrayQueue(array.length);
        System.arraycopy(array, 0, answer.array, 0, array.length);
        answer.right = right;
        answer.left = left;
        answer.size = this.size;
        return answer;
    }


}
