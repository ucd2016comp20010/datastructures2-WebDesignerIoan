package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

import java.util.NoSuchElementException; // added - when out of bounds

public class CircularlyLinkedList<E> implements List<E> {

  private class Node {
    private final E data;
    private Node next;

    public Node(E e, Node n) {
      data = e;
      next = n;
    }

    public E getData() {
      return data;
    }

    public void setNext(Node n) {
      next = n;
    }

    public Node getNext() {
      return next;
    }
  }

  private Node tail = null; // removed final - tail must be able to change
  private int size = 0; // removed final!

  public CircularlyLinkedList() { // Constructor will be empty: We already have the correct empty state -> tail =
                                  // null, size = 0

  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public E get(int i) {

    if (i < 0 || i >= size) {
      throw new IndexOutOfBoundsException("Invalid index: " + i); // we should throw exceptions instead of null!
    }
    Node current = tail.getNext(); // actual head :) -> ! ITS SAFE TO USE GETNEXT() after this, as we know for sure
                                   // that we
                                   // have at least one node (if size is 0, the if makes sure to throw exception
                                   // (think))
    for (int j = 0; j < i; j++) {
      current = current.getNext();
    }
    return current.getData();
  }

  /**
   * Inserts the given element at the specified index of the list, shifting all
   * subsequent elements in the list one position further to make room.
   *
   * @param i the index at which the new element should be stored
   * @param e the new element to be stored
   */
  @Override
  public void add(int i, E e) {
    if (i < 0 || i > size) { // if index is illegal
      throw new IndexOutOfBoundsException("Invalid index: " + i);
    }

    // Case A: empty list (only valid positions are i == 0)
    if (size == 0) {
      Node newest = new Node(e, null); // we create a new node that points to null
      newest.setNext(newest); // points to itself (circular)
      tail = newest; // IMPORTANT: the tail actually becomes also newest (although we have to
                     // instances (newest and tail), they both represent the same thing in this case)
      size = 1;
      return; // stop the method
    }

    Node head = tail.getNext(); // we create the head node (first in line) - it receives the data from the
                                // actual first node (basically becomes it)

    // Case B: insert at front (index 0)
    if (i == 0) {
      Node newest = new Node(e, head); // the new node points to head
      tail.setNext(newest); // new node becomes head (first)
      size++;
      return; // stop the method
    }

    // Case C: insert somewhere after head (including at end)
    Node prev = head; // node used to navigate structure
    for (int k = 1; k < i; k++) { // move to node at index i-1
      prev = prev.getNext();
    } // now prev holds the right address to the node at index i-1

    Node newest = new Node(e, prev.getNext()); // points to node at index i
    prev.setNext(newest); // Now we have officially added the node (newest) to the structure

    // If inserted at the end, update tail
    if (i == size) {
      tail = newest; // TAIL AND LAST NODE ARE THE SAME, so in this case it is "updated" with all the
                     // information of newest
    }

    size++;
  }

  @Override
  public E remove(int i) {
    if (i < 0 || i >= size) { // if index is illegal
      throw new IndexOutOfBoundsException("Invalid index: " + i);
    }
    // removing from a one-element list
    if (size == 1) {
      E removed = tail.getData();// we obtain the data before the node is "deleted"
      tail = null; // tail becomes null (no actual nodes)
      size = 0;
      return removed;
    }

    Node head = tail.getNext(); // node used to navigate structure

    // remove head (index 0)
    if (i == 0) {
      E removed = head.getData();// we obtain the data before the node is "deleted"
      tail.setNext(head.getNext()); // skip old head
      size--;
      return removed;
    }
    // remove index i > 0: find node at i-1
    Node prev = head;
    for (int k = 1; k < i; k++) {
      prev = prev.getNext(); // we go up to index i-1
    }

    Node target = prev.getNext(); // we want to delete node at index i
    E removed = target.getData(); // we obtain the data before the node is "deleted"

    prev.setNext(target.getNext()); // unlink target (i-1 ->[points to] i+1)

    // if we removed the tail, update tail!
    if (target == tail) {
      tail = prev; // tail actually becomes the prev node (i-1)
    }

    size--; // WE HAVE TO DECREMENT SIZE
    return removed;
  }

  public void rotate() {
    if (tail != null) { // or: if (size > 0)
      tail = tail.getNext(); // old head becomes new tail (OBS: we dont actually have a head node (it is
                             // created when needed) - so we can say its updated automatically)
    }
  }

  // ITERATOR: A movable pointer that lets you visit each element exactly once -
  // needed for various methods from now on
  // IMPORTANT: when talking about iterator, we view the structure as a single
  // line, with start and end, even tho we have a circular linked list

  // OBS: You cannot put if/else statements directly inside a class body
  // Inside a class, only these are allowed: field declarations - constructors -
  // methods - initializer blocks

  private class CircularlyLinkedListIterator implements Iterator<E> {
    private Node curr = (tail == null) ? null : tail.getNext(); // we use TERNARY OPERATOR instead of if/else
    private int count = 0; // our current position in the structure

    @Override
    public boolean hasNext() {
      return count < size;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      E res = curr.getData(); // we keep the data of the current to be previous node in the structure
      curr = curr.getNext();
      count++; // we moved to the next node in the structure, so we mark this
      return res;
    }

  }

  @Override
  public Iterator<E> iterator() { // IMPORTANT: it is not called by us, but automatically in certain situations
                                  // (including "implements Ierator<E>")
    return new CircularlyLinkedListIterator();
  }

  public E getFirst() { // helper function - necessary for LinkedCircularQueue
    if (isEmpty())
      return null;
    return tail.getNext().getData(); // head = tail.next
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public E removeFirst() {
    if (size == 0) { // if no nodes
      return null;
    }

    Node head = tail.getNext();
    E removed = head.getData(); // we keep data from to be removed node

    if (size == 1) {
      tail = null; // if we only had one node
    } else {
      tail.setNext(head.getNext()); // skip old head
    }

    size--;
    return removed;
  }

  @Override
  public E removeLast() {
    if (size == 0) {
      throw new NoSuchElementException();
    }

    E removed = tail.getData();

    if (size == 1) {
      tail = null;
    } else {
      Node current = tail.getNext(); // head
      while (current.getNext() != tail) { // we go through the nodes until we get to tail
        current = current.getNext();
      }
      current.setNext(tail.getNext()); // link to head
      tail = current; // move tail backward
    }

    size--; // we decrement size
    return removed;
  }

  @Override
  public void addFirst(E e) {
    Node newest = new Node(e, null); // we create a new node pointing to null

    if (size == 0) {
      newest.setNext(newest); // self-loop - it means we will only have one node
      tail = newest; // tail and the node are basically the same thing
    } else {
      newest.setNext(tail.getNext()); // point to old head
      tail.setNext(newest); // tail now points to new head
    }

    size++; // WE MUST INCREMENT SIZE
  }

  @Override
  public void addLast(E e) {
    Node newest = new Node(e, null);

    if (size == 0) {
      newest.setNext(newest); // self-loop
      tail = newest;
    } else {
      newest.setNext(tail.getNext()); // new node points to head
      tail.setNext(newest); // old tail points to new node
      tail = newest; // move tail
    }

    size++;
  }

  @Override
  public String toString() {
    if (size == 0) { // now we cover empty case
      return "[]";
    }

    StringBuilder sb = new StringBuilder("[");
    Node curr = tail.getNext(); // start at head

    do {
      sb.append(curr.getData());
      curr = curr.getNext();
      if (curr != tail.getNext()) {
        sb.append(", ");
      }
    } while (curr != tail.getNext());

    sb.append("]");
    return sb.toString();
  }

  public static void main(String[] args) {
    CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
    for (int i = 10; i < 20; ++i) {
      ll.addLast(i);
    }

    System.out.println(ll);

    ll.removeFirst();
    System.out.println(ll);

    ll.removeLast();
    System.out.println(ll);

    ll.rotate();
    System.out.println(ll);

    ll.removeFirst();
    ll.rotate();
    System.out.println(ll);

    ll.removeLast();
    ll.rotate();
    System.out.println(ll);

    for (Integer e : ll) {
      System.out.println("value: " + e);
    }

  }
}
