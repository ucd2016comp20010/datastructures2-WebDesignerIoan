package project20280.stacksqueues;

import org.junit.jupiter.api.Test;
import project20280.interfaces.Queue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayQueueTest {

  @Test
  void testSize() {
    Queue<Integer> s = new ArrayQueue<>();
    for (int i = 0; i < 10; ++i)
      s.enqueue(i);
    assertEquals(10, s.size());
  }

  @Test
  void testIsEmpty() {
    Queue<Integer> s = new ArrayQueue<>();
    for (int i = 0; i < 10; ++i)
      s.enqueue(i);
    for (int i = 0; i < 10; ++i)
      s.dequeue();
    assertTrue(s.isEmpty());
  }

  @Test
  void testEnqueue() {
    Queue<Integer> s = new ArrayQueue<>();
    for (int i = 0; i < 10; ++i)
      s.enqueue(i);
    assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", s.toString());
  }

  @Test
  void testFirst() {
    Queue<Integer> s = new ArrayQueue<>();
    for (int i = 0; i < 10; ++i)
      s.enqueue(i);
    assertEquals(Optional.of(0), Optional.ofNullable(s.first())); // modified
  }

  @Test
  void testDequeue() {
    Queue<Integer> s = new ArrayQueue<>();
    for (int i = 0; i < 10; ++i)
      s.enqueue(i);

    assertEquals(Optional.of(0), Optional.ofNullable(s.dequeue())); // modified
    assertEquals(9, s.size());
  }

}
/*
 * Optional:
 * forces the caller to explicitly handle the absence of a value.
 * 
 * Optional.of(value) // value MUST NOT be null
 * Optional.ofNullable(value) // value can be null
 * Optional.empty() // explicitly empty
 */
