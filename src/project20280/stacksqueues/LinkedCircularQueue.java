package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.CircularlyLinkedList;

/**
 * Realization of a circular FIFO queue as an adaptation of a
 * CircularlyLinkedList. This provides one additional method not part of the
 * general Queue interface. A call to rotate() is a more efficient simulation of
 * the combination enqueue(dequeue()). All operations are performed in constant
 * time.
 */

public class LinkedCircularQueue<E> implements Queue<E> {

  private CircularlyLinkedList<E> ll;

  public static void main(String[] args) {
    LinkedCircularQueue<Integer> q = new LinkedCircularQueue<>();

    System.out.println("Empty queue: " + q);

    // Enqueue elements
    for (int i = 1; i <= 5; i++) {
      q.enqueue(i);
    }

    System.out.println("After enqueue 1..5: " + q); // [1, 2, 3, 4, 5]

    // Test first
    System.out.println("First element: " + q.first()); // 1

    // Test dequeue
    System.out.println("Dequeued: " + q.dequeue()); // 1
    System.out.println("After dequeue: " + q); // [2, 3, 4, 5]

    // Test rotate (if implemented)
    q.rotate();
    System.out.println("After rotate: " + q); // [3, 4, 5, 2]

    // Dequeue all
    while (!q.isEmpty()) {
      System.out.println("Removing: " + q.dequeue());
    }

    System.out.println("Final queue (should be empty): " + q);
  }

  public LinkedCircularQueue() { // constructor
    ll = new CircularlyLinkedList<>(); // we initialize the ll variable
  }

  @Override
  public int size() {
    return ll.size(); // we use the existing method
  }

  @Override
  public boolean isEmpty() {
    return ll.isEmpty(); // same
  }

  @Override
  public void enqueue(E e) { // we add at rear (end)
    ll.addLast(e);
  }

  @Override
  public E first() {
    return ll.getFirst(); // using created now helper function
  }

  @Override
  public E dequeue() {
    return ll.removeFirst(); // we take from front (FIFO)
  }

  /**
   * Rotates the front element to the back of the queue.
   * Equivalent to: enqueue(dequeue()), but done in O(1).
   */
  public void rotate() {
    if (!ll.isEmpty()) {
      ll.rotate(); // this should just move the tail pointer (or head pointer) in the CLL
    }
  }

  @Override
  public String toString() {
    return ll.toString();
  }

}
