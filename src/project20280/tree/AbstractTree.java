package project20280.tree;

import project20280.interfaces.Position;
import project20280.interfaces.Tree;
import project20280.interfaces.Queue; // added for bradthfirst
import project20280.stacksqueues.ArrayQueue; // same

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract base class providing some functionality of the Tree interface.
 * <p>
 * The following three methods remain abstract, and must be
 * implemented by a concrete subclass: root, parent, children. Other
 * methods implemented in this class may be overridden to provide a
 * more direct and efficient implementation.
 */
public abstract class AbstractTree<E> implements Tree<E> {

  /**
   * Returns true if Position p has one or more children.
   *
   * @param p A valid Position within the tree
   * @return true if p has at least one child, false otherwise
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public boolean isInternal(Position<E> p) {
    return numChildren(p) > 0; // added: this is how we determine if a node is internal
  }

  /**
   * Returns true if Position p does not have any children.
   *
   * @param p A valid Position within the tree
   * @return true if p has zero children, false otherwise
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public boolean isExternal(Position<E> p) {
    return numChildren(p) == 0;
  }

  /**
   * Returns true if Position p represents the root of the tree.
   *
   * @param p A valid Position within the tree
   * @return true if p is the root of the tree, false otherwise
   */
  @Override
  public boolean isRoot(Position<E> p) {
    return p == root(); // root() comes from Tree interface
  }

  /**
   * Returns the number of children of Position p.
   *
   * @param p A valid Position within the tree
   * @return number of children of Position p
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public int numChildren(Position<E> p) {
    int count = 0;
    for (Position<E> c : children(p)) { // we use the method children() (header is declared in Tree)
      count++;
    }
    return count;
  }

  /**
   * Returns the number of nodes in the tree.
   *
   * @return number of nodes in the tree
   */
  @Override
  public int size() {
    int count = 0;
    for (Position p : positions()) // positions() is from Tree interface
      count++;
    return count;
  }

  /**
   * Tests whether the tree is empty.
   *
   * @return true if the tree is empty, false otherwise
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  // ---------- support for computing depth of nodes and height of (sub)trees
  // ----------

  /**
   * Returns the number of levels separating Position p from the root.
   *
   * @param p A valid Position within the tree
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  public int depth(Position<E> p) throws IllegalArgumentException {
    if (isRoot(p)) // recursive implementation using the depth() method
      return 0;
    return 1 + depth(parent(p));
  }

  /**
   * Returns the height of the tree.
   * <p>
   * Note: This implementation works, but runs in O(n^2) worst-case time.
   */
  private int heightBad() { // works, but quadratic worst-case time
    int h = 0;
    for (Position<E> p : positions())
      if (isExternal(p)) // only consider leaf positions
        h = Math.max(h, depth(p)); // by doing this we always get the deepest leaf in the end
    return h;
  }

  public int height_recursive(Position<E> p) {
    int h = 0; // this stores the maximum height among nodes p children
    for (Position<E> c : children(p)) { // we look at each child
      h = Math.max(h, 1 + height_recursive(c)); // we want the longest path (between children) from eventual leaf to
                                                // root - we add 1 each time we are not at a leaf (if leaf, recursive
                                                // call returns h as 0 because we dont enter for)
    }
    return h;
  }

  /**
   * Returns the height of the subtree rooted at Position p.
   *
   * @param p A valid Position within the tree
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  public int height() throws IllegalArgumentException {
    return height_recursive(root());
  }

  // ---------- support for various iterations of a tree ----------

  // ---------------- nested ElementIterator class ----------------
  /*
   * This class adapts the iteration produced by positions() to return elements.
   */
  private class ElementIterator implements Iterator<E> {
    Iterator<Position<E>> posIterator = positions().iterator();

    public boolean hasNext() {
      return posIterator.hasNext();
    }

    public E next() {
      return posIterator.next().getElement();
    } // return element!

    public void remove() {
      posIterator.remove();
    }
  }

  /**
   * Returns an iterator of the elements stored in the tree.
   *
   * @return iterator of the tree's elements
   */
  @Override
  public Iterator<E> iterator() {
    return new ElementIterator();
  }

  /**
   * Returns an iterable collection of the positions of the tree.
   *
   * @return iterable collection of the tree's positions
   */
  @Override
  public Iterable<Position<E>> positions() {
    return preorder();
  }

  /**
   * Adds positions of the subtree rooted at Position p to the given
   * snapshot using a preorder traversal
   *
   * @param p        Position serving as the root of a subtree
   * @param snapshot a list to which results are appended
   */
  private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) { // Preorder = visit node, then each child
    snapshot.add(p); // visit p first
    for (Position<E> c : children(p)) // then traverse children
      preorderSubtree(c, snapshot);
  }

  /**
   * Returns an iterable collection of positions of the tree, reported in
   * preorder.
   *
   * @return iterable collection of the tree's positions in preorder
   */
  public Iterable<Position<E>> preorder() {
    List<Position<E>> snapshot = new ArrayList<>();
    if (!isEmpty()) // if we have root
      preorderSubtree(root(), snapshot); // we use the helper function preorderSubtree from the root, basically going
                                         // through all nodes and their children
    return snapshot; // we return the list
  }

  /**
   * Adds positions of the subtree rooted at Position p to the given
   * snapshot using a postorder traversal
   *
   * @param p        Position serving as the root of a subtree
   * @param snapshot a list to which results are appended
   */
  private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) { // Postorder = traverse children first,
                                                                             // then visit node
    for (Position<E> c : children(p)) // we first visit children
      postorderSubtree(c, snapshot); // recursive call for child
    snapshot.add(p); // in the end we add the parent to the list
  }

  /**
   * Returns an iterable collection of positions of the tree, reported in
   * postorder.
   *
   * @return iterable collection of the tree's positions in postorder
   */
  public Iterable<Position<E>> postorder() {
    List<Position<E>> snapshot = new ArrayList<>();
    if (!isEmpty())
      postorderSubtree(root(), snapshot); // kickstart the "chain" reaction from root, and so visiting each childrens
                                          // and node
    return snapshot;
  }

  /**
   * Returns an iterable collection of positions of the tree in breadth-first
   * order.
   *
   * @return iterable collection of the tree's positions in breadth-first order
   */
  public Iterable<Position<E>> breadthfirst() { // we look "on levels" of tree
    List<Position<E>> snapshot = new ArrayList<>();
    if (isEmpty()) // if no nodes
      return snapshot;

    Queue<Position<E>> q = new ArrayQueue<>(); // we create this queue for the way we visit each level
    q.enqueue(root()); // to start we add the root

    while (!q.isEmpty()) { // as long as the queue is not empty (levels have not finished)
      Position<E> p = q.dequeue(); // we take from front of queue
      snapshot.add(p); // and add to the snapshot

      for (Position<E> c : children(p)) { // we go through each child of the current node
        q.enqueue(c); // we add it to the queue (populating new level)
      }
    }
    return snapshot;
  }
  // OBS: because we use a queue, even when we have a brother and kids on the same
  // queue, we will still add the brother to the snapshot before the kids (we take
  // from front of queue and add to the back)
}
