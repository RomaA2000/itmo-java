package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    protected LinkedQueue copyQueueImplementation() {
        LinkedQueue answer = new LinkedQueue();
        for (Node iterator = head; iterator != null; iterator = iterator.next) {
            answer.enqueue(iterator.information);
        }
        return answer;
    }

    private class Node {
        Node next;
        Object information;
        Node (Object informationOfNextElement, Node nextElement) {
            next = nextElement;
            information = informationOfNextElement;
        }
    }

    void enqueueImplementation(Object element) {
        Node newNode = new Node(element,null);
        if (size == 0) {
            tail = head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    Object elementImplementation() {
        return head.information;
    }

    void dequeueImplementation() {
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            head = head.next;
        }
    }

    void clearImplementation() {
        tail = null;
        head = null;
    }
}
