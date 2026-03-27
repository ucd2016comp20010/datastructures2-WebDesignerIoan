package project20280.priorityqueue;

/*
 */

import project20280.interfaces.Entry;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {

  protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

  /**
   * Creates an empty priority queue based on the natural ordering of its keys.
   */
  public HeapPriorityQueue() {
    super();
  }

  /**
   * Creates an empty priority queue using the given comparator to order keys.
   *
   * @param comp comparator defining the order of keys in the priority queue
   */
  public HeapPriorityQueue(Comparator<K> comp) {
    super(comp);
  }

  /**
   * Creates a priority queue initialized with the respective key-value pairs. The
   * two arrays given will be paired element-by-element. They are presumed to have
   * the same length. (If not, entries will be created only up to the length of
   * the shorter of the arrays)
   *
   * @param keys   an array of the initial keys for the priority queue
   * @param values an array of the initial values for the priority queue
   */
  public HeapPriorityQueue(K[] keys, V[] values) { // we are creating a priority queue (heap) from the two pair arrays
    super();
    int n = Math.min(keys.length, values.length); // we check which array is smaller
    for (int i = 0; i < n; i++) {
      checkKey(keys[i]); // as long as the key (index -> priority) is valid
      heap.add(new PQEntry<>(keys[i], values[i])); // we add a new entry (pair)
    }
    heapify(); // O(n) construction of heap from array
  }

  // protected utilities
  // Based on maths formulas
  /*
   * THIS IS TO SIMPLY KNOW HOW TO CREATE A BINARY TREE FROM AN ARRAY!
   * Nodes are packed level-by-level:
   * Level 0: index 0
   * Level 1: index 1, 2
   * Level 2: index 3, 4, 5, 6
   * This is just the index of the nodes, not the value of them! OBS: each level
   * doubles in size
   * EXAMPLE: Take node at index 4 (say value = 10): parent(4) = (4 - 1) / 2 = 3 /
   * 2 =
   * 1 -> PARENT IS INDEX 1 (and whatever value was there)
   */
  protected int parent(int j) {
    return (j - 1) / 2;
  }

  protected int left(int j) {
    return 2 * j + 1; // formula for index of left child (can be checked)
  }

  protected int right(int j) {
    return 2 * j + 2;
  }

  // Does the node at index j have a left child? - this is the check
  protected boolean hasLeft(int j) {
    return left(j) < heap.size(); // as long as the resulted value (from left(j)) is still in the index range
  }
  /*
   * EXAMPLE: [2, 4, 16, 5, 10]
   * Size is 5, take j=2; left(2) = 2*2 + 1 = 5; but 5<5 false => 5 does not have
   * a kid!
   */

  // Does the node at index j have a right child? - this is the check
  protected boolean hasRight(int j) {
    return right(j) < heap.size();
  }

  /**
   * Exchanges the entries at indices i and j of the array list.
   */
  // Swaps two elements in the heap array -> helper
  protected void swap(int i, int j) {
    Entry<K, V> temp = heap.get(i);
    heap.set(i, heap.get(j));
    heap.set(j, temp);
  }

  /**
   * Moves the entry at index j higher, if necessary, to restore the heap
   * property.
   */
  /*
   * This is used after insertion. (basically checks if the child has bigger value
   * than parent, else switch)
   * When you insert into a heap:
   * - put new element at the end
   * - the tree shape is still valid
   * - but heap order may be broken
   * So upheap moves the new element up toward the root until the min-heap
   * property is restored.
   */
  protected void upheap(int j) {
    while (j > 0) { // keep going while the node is not the root
      int p = parent(j);
      if (compare(heap.get(j), heap.get(p)) >= 0)
        break; // if we find that the condition is validated we break and exit the while loop
               // (the node is in the valid and right position)
      swap(j, p); // if not, the node swaps with its parent
      j = p; // index becomes the index of parent, and we continue the checks for the next
             // level
    }
  }
  /*
   * Upheap SCENARIO: heap = [2, 5, 4, 10]
   * - we insert 3 at end: [2, 5, 4, 10, 3]
   * - j is 4 (index), and parent(j) is 1 (index) => we compare 5 with 3 => 5 > 3
   * (swap needed) => heap = [2, 3, 4, 10, 5], and WE UPDATE index from 4 to 1.
   * Now we perform the check again for the new index, but it will stop
   */

  /**
   * Moves the entry at index j lower, if necessary, to restore the heap property.
   */
  /*
   * This is used after removeMin. When you remove the minimum:
   * - remove root
   * - move last element to root
   * - tree shape is still valid
   * - but heap order may now be broken at the top
   * => downheap moves that element down the tree until heap order is restored.
   * RULE: in a min heap, if we swap downward we must swap with SMALLER CHILD
   */
  protected void downheap(int j) {
    while (hasLeft(j)) { // If there is no left child, then there are no children at all in a complete
                         // binary tree, so we stop
      int leftIndex = left(j);
      int smallChildIndex = leftIndex; // At first, we assume the smaller child is the left child.

      if (hasRight(j)) { // if right child exists
        int rightIndex = right(j);
        if (compare(heap.get(rightIndex), heap.get(leftIndex)) < 0) {
          smallChildIndex = rightIndex; // if right child is smaller, use it instead
        }
      }
      // at this point "smallChildIndex" is the index of the smaller child (between
      // left and right)

      if (compare(heap.get(smallChildIndex), heap.get(j)) >= 0)
        break; // parent <= both children -> heap order is valid

      swap(j, smallChildIndex); // swap downward
      j = smallChildIndex; // change index, and continue verification at next lower level
    }
  }

  /*
   * Downheap SCENARIO: Suppose after removing min, heap becomes: [15, 4, 16, 5,
   * 10, 23, 39]
   * This is wrong because root 15 is bigger than child 4.
   * Start with j = 0 -> smallest child has value 4 -> 4 < 15 so swap -> heap: [4,
   * 15, 16, 5, 10, 23, 39] AND j becomes 1
   * j = 1 -> smallest child has value 5 -> 5 < 15 so swap -> heap: [4, 5, 16, 15,
   * 10, 23, 39] AND j becomes 3
   * j = 3 -> No left child, stop. -> Final heap is valid.
   */

  /**
   * Performs a bottom-up construction of the heap in linear time.
   */
  /*
   * IMPORTANT: This is the fast heap construction method.
   * Instead of inserting elements one by one, it takes an ARRAY/LIST that already
   * contains all entries and turns it into a heap in O(n) time.
   */
  protected void heapify() {
    int startIndex = parent(heap.size() - 1); // this is the parent of the last index in array (leafs are already valid)
                                              // -> all nodes between ⌊n/2⌋ to n-1 are leafs
    for (int j = startIndex; j >= 0; j--) { // we go through each internal node (starting from startIndex, each level
                                            // has at
                                            // least one child)
      downheap(j); // make sure j is in right place, or place it there (so that the heap condition
                   // is valid)
    }
  }

  /*
   * We move from bottom to top.
   * 
   * That is important because when we call downheap(j), we want the subtrees
   * below j to already be heaps.
   * 
   * By processing bottom-up, that is guaranteed.
   * 
   * SCENARIO: heap = [35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5]
   * heap.size() - 1 = 11 (last element) => parent(11) = (11 - 1) / 2 = 10 / 2 = 5
   * => startIndex = 5
   */

  // public methods

  /**
   * Returns the number of items in the priority queue.
   *
   * @return number of items
   */
  @Override
  public int size() {
    return heap.size();
  }

  /**
   * Returns (but does not remove) an entry with minimal key.
   *
   * @return entry having a minimal key (or null if empty)
   */
  @Override
  public Entry<K, V> min() {
    if (heap.isEmpty())
      return null;
    return heap.get(0);
  }

  /**
   * Inserts a key-value pair and return the entry created.
   *
   * @param key   the key of the new entry
   * @param value the associated value of the new entry
   * @return the entry storing the new key-value pair
   * @throws IllegalArgumentException if the key is unacceptable for this queue
   */

  /*
   * When inserting into a heap:
   * - Check key is valid (index exists)
   * - Create new entry
   * - Add it to the END of the array
   * - Fix heap using upheap
   */
  @Override
  public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
    checkKey(key); // ensure key is valid for comparasion

    Entry<K, V> newest = new PQEntry<>(key, value); // create entry
    heap.add(newest); // add to end

    upheap(heap.size() - 1); // restore heap property (upheap the last index)

    return newest; // return the new Entry (with updated K and V)
  }

  /**
   * Removes and returns an entry with minimal key.
   *
   * @return the removed entry (or null if empty)
   */
  /*
   * - If empty → return null
   * - Save root (min element)
   * - Move last element to root
   * - Remove last element
   * - Fix heap using downheap
   * - Return saved root
   */
  @Override
  public Entry<K, V> removeMin() {
    if (heap.isEmpty())
      return null;

    Entry<K, V> answer = heap.get(0); // save min

    swap(0, heap.size() - 1); // move last to root
    heap.remove(heap.size() - 1); // remove last

    if (!heap.isEmpty()) {
      downheap(0); // restore heap
    }

    return answer;
  }

  public String toString() {
    return heap.toString();
  }

  /**
   * Used for debugging purposes only
   */
  private void sanityCheck() {
    for (int j = 0; j < heap.size(); j++) {
      int left = left(j);
      int right = right(j);
      // System.out.println("-> " +left + ", " + j + ", " + right);
      Entry<K, V> e_left, e_right;
      e_left = left < heap.size() ? heap.get(left) : null;
      e_right = right < heap.size() ? heap.get(right) : null;
      if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0) {
        System.out.println("Invalid left child relationship");
        System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
      }
      if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0) {
        System.out.println("Invalid right child relationship");
        System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
      }
    }
  }

  public static void main(String[] args) {
    Integer[] rands = new Integer[] { 35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5 };
    HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(rands, rands); // we build heap here

    System.out.println("elements: " + java.util.Arrays.toString(rands));
    System.out.println("after adding elements: " + pq);

    System.out.println("min element: " + pq.min());

    pq.removeMin(); // we remove min element and fix heap
    System.out.println("after removeMin: " + pq);
    // [ 1,
    // 2, 4,
    // 23, 21, 5, 12,
    // 24, 26, 35, 33, 15]

    // Q6: We create a random array, we transform it into a heap by adding elements
    // one by one, and then we removeMin for every element and store the resulting
    // element in a new array. This resuling array will be sorted, because the
    // minimum element in a heap will always be the root, even tho internal nodes
    // (on same level) might not be sorted.

    // Q7: The previous algorithm has to copy each element to a new array, but we
    // could just do the sorting in place. First we use heapify, but the array is
    // not yet sorted. OBS: if we want ascending order, we use a MAX HEAP, and we
    // modify the downheap method. Basically we make sure root is largest number,
    // and for the downheap method we have an extra variable (end), that represents
    // the range (from first element to a continuously shrinking end index). After
    // we take the max element (root), we switch with end element, we do end-- (so
    // that the max element in last position will not be modified) and we correct
    // the heap using downheap of new root.
  }
}
