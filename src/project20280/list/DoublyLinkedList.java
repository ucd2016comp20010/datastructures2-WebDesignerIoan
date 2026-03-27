package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

import java.util.NoSuchElementException; // added - when out of bounds

public class DoublyLinkedList<E> implements List<E> {

  private static class Node<E> {
    private final E data;
    private Node<E> next;
    private Node<E> prev; // prev cannot be final

    public Node(E e, Node<E> p, Node<E> n) { // constructor
      data = e;
      prev = p;
      next = n;
    }

    public E getData() {
      return data;
    }

    public Node<E> getNext() {
      return next;
    }

    public Node<E> getPrev() {
      return prev;
    }

  }

  private final Node<E> head;
  private final Node<E> tail;
  private int size = 0; // size cannot be final!

  public DoublyLinkedList() { // constructor
    head = new Node<E>(null, null, null);
    tail = new Node<E>(null, head, null);
    head.next = tail;
    tail.prev = head; // added
  }

  private void addBetween(E e, Node<E> pred, Node<E> succ) {
    Node<E> newest = new Node<>(e, pred, succ); // we have to first create a new node with the e data, and the
                                                // constructor will take care of assigning the node's addresses (front
                                                // and back)
    pred.next = newest;
    succ.prev = newest;
    size++; // new node has been added => increment size
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public E get(int i) {
    Node<E> current = head.getNext(); // head IS A SENTINEL NODE => first real node is head.next
    if (i < 0 || i >= size) {
      throw new IndexOutOfBoundsException("Invalid index: " + i); // we should throw exceptions instead of null!
    }
    for (int j = 0; j < i; j++) {
      current = current.getNext();
    }
    return current.getData();
  }

  @Override
  public void add(int i, E e) {
    Node<E> current = head.getNext(); // node currently at index 0
    if (i < 0 || i > size) { // we should be able to add at end
      throw new IndexOutOfBoundsException("Invalid index: " + i); // we should throw exceptions instead of null!
    }
    for (int j = 0; j < i; j++) {
      current = current.getNext();
    } // after loop, current becomes node at index i, but we want to add the new node
      // before it!
    Node<E> previous = current.getPrev(); // now that we have the previous and current node, we can insert between them
    addBetween(e, previous, current); // we use the method created, that also updates size
  }

  @Override
  public E remove(int i) {
    Node<E> current = head.getNext(); // node currently at index 0
    if (i < 0 || i >= size) { // we should be able to add at end
      throw new IndexOutOfBoundsException("Invalid index: " + i); // we should throw exceptions instead of null!
    }
    for (int j = 0; j < i; j++) {
      current = current.getNext();
    }
    current.getPrev().next = current.getNext(); // we link the previous node to the next (assuming current is in
                                                // between)
    current.getNext().prev = current.getPrev(); // we link the next node the the previous (current in middle)
    size--; // we update the size
    return current.getData(); // we return E data
  }

  private class DoublyLinkedListIterator implements Iterator<E> { // removed - This <E> (that was present) is a new
                                                                  // generic type, not the same E
                                                                  // as your outer DoublyLinkedList<E>. - That
                                                                  // creates a type mismatch and forces you to cast
    Node<E> curr = head.next;

    @Override
    public boolean hasNext() {
      return curr != tail;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      E res = curr.data; // SHOULD HAVE USED GETTERS - ENCAPSULATION!
      curr = curr.next;
      return res;
    }
  }

  @Override
  public Iterator<E> iterator() { // constructor - iterator is A cursor that moves through a collection one
                                  // element at a time. (It is NOT the collection itself)
    return new DoublyLinkedListIterator(); // we dont need <E>
  }

  private E remove(Node<E> n) {
    if (n == head || n == tail) { // we cannot remove the head or tail (sentinels)
      throw new IllegalArgumentException("Cannot remove sentinel node");
    }

    Node<E> predecessor = n.getPrev(); // we create two nodes, that represent the previous and next node
    Node<E> successor = n.getNext();

    predecessor.next = successor; // we link them with each other
    successor.prev = predecessor;

    size--; // we decrement

    return n.getData(); // we must return data E
  }

  public E first() {
    if (isEmpty()) {
      return null;
    }
    return head.next.getData(); // first real node
  }

  public E last() {
    if (isEmpty()) {
      return null;
    }
    return tail.getPrev().getData(); // last real node
  }

  @Override
  public E removeFirst() {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException("List is empty"); // exception type is important
    }
    return remove(head.getNext()); // we use the already created method on the first real node
  }

  @Override
  public E removeLast() {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException("List is empty"); // exception type is important
    }
    return remove(tail.getPrev()); // we use the already created method, that return E
  }

  @Override
  public void addLast(E e) {
    addBetween(e, tail.getPrev(), tail); // we already have a beautiful method for this:)
  }

  @Override
  public void addFirst(E e) {
    addBetween(e, head, head.getNext());
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    Node<E> curr = head.next;
    while (curr != tail) {
      sb.append(curr.data);
      curr = curr.next;
      if (curr != tail) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  public static void main(String[] args) {
    DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
    ll.addFirst(0);
    ll.addFirst(1);
    ll.addFirst(2);
    ll.addLast(-1);
    System.out.println(ll);

    ll.removeFirst();
    System.out.println(ll);

    ll.removeLast();
    System.out.println(ll);

    for (Integer e : ll) {
      System.out.println("value: " + e);
    }
  }
}
