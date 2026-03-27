package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

  private DoublyLinkedList<E> ll;

  public final static void main(String[] args) {
  }

  public LinkedQueue() { // constructor
    ll = new DoublyLinkedList<>(); // we initialize the ll variable
  }

  @Override
  public int size() {
    return ll.size();
  }

  @Override
  public boolean isEmpty() {
    return ll.isEmpty();
  }

  @Override
  public void enqueue(E e) { // IMPORTANT: enqueue at rear
    ll.addLast(e); // we use the method already created for Doubly Linked List
  }

  @Override
  public E first() {
    return ll.first(); // gets the first element, using the method defined in the DoublyLinkedList
                       // Class. Null case is handled in that method
  }

  @Override
  public E dequeue() { // IMPORTANT: dequeue from front (think about the visual example)
    return ll.removeFirst(); // we use the method already created for Doubly Linked List
  }

  public String toString() {
    return ll.toString();
  }
}
/*
 * QUEUE USES FIFO
 * enqueue → add at the back
 * dequeue → remove from the front
 * Front of queue = first element
 * Rear of queue = last element
 */
