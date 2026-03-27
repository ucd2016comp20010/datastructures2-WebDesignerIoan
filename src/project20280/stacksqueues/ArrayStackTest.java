package project20280.stacksqueues;

import org.junit.jupiter.api.Test;
import project20280.interfaces.Stack;

import java.util.Optional; // we use it as a box that either contains or deos not contain an element

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayStackTest {
  @Test
  void testSize() {
    Stack<Integer> s = new ArrayStack<>();

    assertEquals(0, s.size());

    int N = 16;
    for (int i = 0; i < N; ++i)
      s.push(i);
    assertEquals(N, s.size());
  }

  @Test
  void testIsEmpty() {
    Stack<Integer> s = new ArrayStack<>();
    for (int i = 0; i < 10; ++i)
      s.push(i);
    for (int i = 0; i < 10; ++i) {
      s.pop();
    }
    assertTrue(s.isEmpty());
  }

  @Test
  void testPush() {
    Stack<Integer> s = new ArrayStack<>();
    for (int i = 0; i < 10; ++i)
      s.push(i);
    assertEquals(10, s.size());
    assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
  }

  @Test
  void testTop() {
    Stack<Integer> s = new ArrayStack<>();
    for (int i = 0; i < 10; ++i)
      s.push(i);
    assertEquals(Optional.of(9), Optional.ofNullable(s.top())); // Modified: we cast int to optional (tests will fail
                                                                // otherwise)
  }

  @Test
  void testPop() {
    Stack<Integer> s = new ArrayStack<>();
    for (int i = 0; i < 10; ++i)
      s.push(i);
    assertEquals(Optional.of(9), Optional.ofNullable(s.pop())); // Same thing
    assertEquals(9, s.size());
  }

  @Test
  void testToString() {
    Stack<Integer> s = new ArrayStack<>();
    for (int i = 0; i < 10; ++i)
      s.push(i);
    assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
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
