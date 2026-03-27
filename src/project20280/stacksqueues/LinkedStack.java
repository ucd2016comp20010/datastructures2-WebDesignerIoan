package project20280.stacksqueues;

import project20280.interfaces.Stack;
import project20280.list.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {

  private final DoublyLinkedList<E> ll; // we modify for encapsulation

  public static void main(String[] args) {
  }

  public LinkedStack() { // constructor
    ll = new DoublyLinkedList<>(); // we initialize the ll variable
  }

  @Override
  public int size() {
    return ll.size(); // obs: we use the size() method from DoublyLinkedList.java file (class)
  }

  @Override
  public boolean isEmpty() {
    return ll.isEmpty();
  }

  @Override
  public void push(E e) {
    ll.addFirst(e); // top of stack = first in list
  }

  @Override
  public E top() {
    return ll.first(); // gets the first element, using the method defined in the DoublyLinkedList
                       // Class. Null case is handled in that method
  }

  @Override
  public E pop() {
    if (ll.isEmpty()) {
      return null; // we make this check, because the method throws IndexOutOfBoundsException
    }
    return ll.removeFirst();
  }

  public String toString() {
    return ll.toString(); // we use the already defined method
  }
}
