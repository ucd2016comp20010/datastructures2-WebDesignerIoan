package project20280.tree;

import project20280.interfaces.BinaryTree;
import project20280.interfaces.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class providing some functionality of the BinaryTree
 * interface.
 * <p>
 * The following five methods remain abstract, and must be implemented
 * by a concrete subclass: size, root, parent, left, right.
 */
public abstract class AbstractBinaryTree<E> extends AbstractTree<E>
    implements BinaryTree<E> {

  /**
   * Returns the Position of p's sibling (or null if no sibling exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the sibling (or null if no sibling exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  @Override
  public Position<E> sibling(Position<E> p) {
    Position<E> parent = parent(p); // this is the parent of node p
    if (parent == null)
      return null; // p is root
    if (p == left(parent)) // if the node is the left child of the parent
      return right(parent); // p is left child - sibling is right
    else
      return left(parent); // p is right child (or not left) - sibling is left
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
    if (left(p) != null) // if there is left child
      count++;
    if (right(p) != null) // if there is right child
      count++;
    return count;
  }

  /**
   * Returns an iterable collection of the Positions representing p's children.
   *
   * @param p A valid Position within the tree
   * @return iterable collection of the Positions of p's children
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public Iterable<Position<E>> children(Position<E> p) {
    List<Position<E>> snapshot = new ArrayList<>(2); // max capacity of 2
    if (left(p) != null)
      snapshot.add(left(p)); // if left kid exists, we add to the list (snapshot) of children
    if (right(p) != null)
      snapshot.add(right(p)); // if right kid exists, we add to the list of children
    return snapshot;
  }

  /**
   * Adds positions of the subtree rooted at Position p to the given
   * snapshot using an inorder traversal
   *
   * @param p        Position serving as the root of a subtree
   * @param snapshot a list to which results are appended
   */
  private void inorderSubtree(Position<E> p, List<Position<E>> snapshot) { // inorder traversal means visiting left kid
                                                                           // (absolute maximum left until leaf),
                                                                           // node (parent of such leaf), right kid from
                                                                           // the parent of the leaf (left node)
    if (left(p) != null) // if left kid exists
      inorderSubtree(left(p), snapshot); // we recursivelly call again (we go as low as we can on left side)

    snapshot.add(p); // once we reached maximum left path, we add parent of leaf

    if (right(p) != null)
      inorderSubtree(right(p), snapshot); // we also visit right at end
  }

  /**
   * Returns an iterable collection of positions of the tree, reported in inorder.
   *
   * @return iterable collection of the tree's positions reported in inorder
   */
  public Iterable<Position<E>> inorder() {
    List<Position<E>> snapshot = new ArrayList<>();
    if (!isEmpty())
      inorderSubtree(root(), snapshot); // fill the snapshot recursively - kickstart chain reaction
    return snapshot;
  }

  /**
   * Returns an iterable collection of the positions of the tree using inorder
   * traversal
   *
   * @return iterable collection of the tree's positions using inorder traversal
   */
  @Override
  public Iterable<Position<E>> positions() {
    return inorder();
  }
}
