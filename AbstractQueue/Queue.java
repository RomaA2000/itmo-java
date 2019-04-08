package queue;

import java.util.function.Function;
import java.util.function.Predicate;

/*We will consider the queue as an array with the last one added at the end
  and the earliest one added at the beginning
  all its elements != null
  its size >= 0
*/
public interface Queue {

    //Pre: element != null
    void enqueue(Object element);
    //Post: last in Queue = element && other elements unchanged

    //Pre: Queue size > 0 && queue elements != null
    Object element();
    //Post: other elements unchanged && returned the earliest added element

    //Pre: Queue size > 0;
    Object dequeue();
    //Post: Returned and deleted the earliest added element && other elements unchanged && size' = size - 1;

    //Pre:true
    int size();
    //Returned the queue size &&  other elements unchanged

    //Pre: true
    boolean isEmpty();
    //Post: if the queue size == 0 returned true, else - false &&  other elements unchanged

    //Pre: true
    void clear();
    //Post: Queue size = 0

    //Pre: Queue size > 0 && queue elements != null
    AbstractQueue filter(Predicate<Object> predicate);
    //Post: Queue size >= 0 && for each i Predicate.test(Queue'[i]) = true && elements in Queue unchanged && relative order is the same as in Queue'

    //Pre: Queue size > 0 && queue elements != null
    AbstractQueue map(Function<Object, Object> function);
    //Post: for each i Queue'[i] = function(Queue[i]) && elements in Queue unchanged

}
