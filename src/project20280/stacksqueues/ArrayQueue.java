package project20280.stacksqueues;

import project20280.interfaces.Queue;

public class ArrayQueue<E> implements Queue<E> {

  private static final int CAPACITY = 1000;
  private E[] data;
  private int front = 0; // modified - must not be final
  private int size = 0; // modified

  public ArrayQueue(int capacity) {
    if (capacity < 0)
      throw new IllegalArgumentException("Capacity must be non-negative");
    data = (E[]) new Object[capacity]; // create Object[], then cast it to E[]
  }

  public ArrayQueue() {
    this(CAPACITY);
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  // adding a new node at the rear (end) **
  @Override
  public void enqueue(E e) {
    if (size == data.length) { // if we have filled all available spots in the array
      throw new IllegalStateException("Queue is full");
    }
    int avail = (front + size) % data.length; // next free slot at the rear - if even wraps around the beginning
    data[avail] = e;
    size++; // we increment the size
  }

  @Override
  public E first() {
    return isEmpty() ? null : data[front];
  }

  // removing an element from the queue (from the start)
  @Override
  public E dequeue() {
    if (isEmpty()) // if no elements
      return null;
    E answer = data[front]; // we make a copy of the data of the first node
    data[front] = null; // help GC
    front = (front + 1) % data.length; // circular advance - quite interesting, it acts as a normal increment most of
                                       // the time, but if the sum "front + 1" is larger than the number of present
                                       // elements, the front index wraps around !
    size--; // we decrement the size
    return answer;
  }

  public String toString() { // VERY IMPORTANT
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < size; ++i) {
      E res = data[(front + i) % data.length]; // here we obtain the RIGHT element (accounting for wrapping around and
      // different front indexes)
      sb.append(res);
      if (i != size - 1) // if we are not at the last such elem
        sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
  }

  public static void main(String[] args) {
    Queue<Integer> qq = new ArrayQueue<>();
    System.out.println(qq);

    int N = 10;
    for (int i = 0; i < N; ++i) {
      qq.enqueue(i);
    }
    System.out.println(qq);

    for (int i = 0; i < N / 2; ++i)
      qq.dequeue();
    System.out.println(qq);

  }
}
