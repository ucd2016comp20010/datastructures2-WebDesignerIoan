package project20280.tree;

import project20280.interfaces.Entry;
import project20280.interfaces.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;

/**
 * An implementation of a sorted map using a binary search tree.
 */

public class TreeMap<K, V> extends AbstractSortedMap<K, V> {

  // ---------------- nested BalanceableBinaryTree class ----------------

  /**
   * A specialized version of the LinkedBinaryTree class with additional mutators
   * to support binary search tree operations, and a specialized node class that
   * includes an auxiliary instance variable for balancing data.
   */
  protected static class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {
    // -------------- nested BSTNode class --------------
    // this extends the inherited LinkedBinaryTree.Node class
    protected static class BSTNode<E> extends Node<E> {
      int aux = 0;

      BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
        super(e, parent, leftChild, rightChild);
      }

      public int getAux() {
        return aux;
      }

      public void setAux(int value) {
        aux = value;
      }
    } // --------- end of nested BSTNode class ---------

    // positional-based methods related to aux field
    public int getAux(Position<Entry<K, V>> p) {
      return ((BSTNode<Entry<K, V>>) p).getAux();
    }

    public void setAux(Position<Entry<K, V>> p, int value) {
      ((BSTNode<Entry<K, V>>) p).setAux(value);
    }

    // Override node factory function to produce a BSTNode (rather than a Node)
    @Override
    protected Node<Entry<K, V>> createNode(Entry<K, V> e, Node<Entry<K, V>> parent, Node<Entry<K, V>> left,
        Node<Entry<K, V>> right) {
      return new BSTNode<>(e, parent, left, right);
    }

    /**
     * Relinks a parent node with its oriented child node.
     */
    private void relink(Node<Entry<K, V>> parent, Node<Entry<K, V>> child, boolean makeLeftChild) { /* ADDED - */
      child.setParent(parent); // we link it officially
      if (makeLeftChild) {
        parent.setLeft(child); // we also link the other way around
      } else {
        parent.setRight(child);
      }
    }

    /**
     * Rotates Position p above its parent. Switches between these configurations,
     * depending on whether p is a or p is b.
     *
     * <pre>
     *          b                  a
     *         / \                / \
     *        a  t2             t0   b
     *       / \                    / \
     *      t0  t1                 t1  t2
     * </pre>
     * <p>
     * Caller should ensure that p is not the root.
     */
    /**
     * Rotates Position p above its parent.
     */
    public void rotate(Position<Entry<K, V>> p) {
      Node<Entry<K, V>> x = validate(p);
      Node<Entry<K, V>> y = x.getParent(); // parent
      Node<Entry<K, V>> z = y.getParent(); // grandparent (possibly null)

      // HOW X CONNECTS TO THE REST OF THE TREE
      if (z == null) { // it means y was previous root, and now x will become root
        root = x; // we rotate, so x becomes root and will have
        x.setParent(null);
      } else {
        relink(z, x, y == z.getLeft()); // Connects x to z in the same position where y used to be, (it looks like we
                                        // lose y, but we will reconect it to x in the next part!!!)
      }

      // now rotate x and y, including transfer of middle subtree
      if (x == y.getLeft()) { // right rotation - if x is the left child of y
        relink(y, x.getRight(), true); // first, we make sure the right subtree of x is transfered to y
        relink(x, y, false); // then we actually swap them conceptually (IMPORTANT: SEE TREE EXAMPLE ABOVE)
      } else { // left rotation - if x is the right child of y
        relink(y, x.getLeft(), false); // same (as explained through the tree above, but the other way around)
        relink(x, y, true);
      }

    }

    /**
     * Returns the Position that becomes the root of the restructured subtree.
     * <p>
     * Assumes the nodes are in one of the following configurations:
     *
     * <pre>
     *     z=a                 z=c           z=a               z=c
     *    /  \                /  \          /  \              /  \
     *   t0  y=b             y=b  t3       t0   y=c          y=a  t3
     *      /  \            /  \               /  \         /  \
     *     t1  x=c         x=a  t2            x=b  t3      t0   x=b
     *        /  \        /  \               /  \              /  \
     *       t2  t3      t0  t1             t1  t2            t1  t2
     * </pre>
     * <p>
     * The subtree will be restructured so that the node with key b becomes its
     * root.
     *
     * <pre>
     *           b
     *         /   \
     *       a       c
     *      / \     / \
     *     t0  t1  t2  t3
     * </pre>
     * <p>
     * Caller should ensure that x has a grandparent.
     */
    /**
     * Returns the Position that becomes the root of the restructured subtree.
     */
    public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
      Position<Entry<K, V>> y = parent(x);
      Position<Entry<K, V>> z = parent(y);

      if ((x == right(y)) == (y == right(z))) { // in straight case → y is new root
        rotate(y); // matching alignment: single rotation
        return y;
      } else { // in zig-zag case → x is new root
        rotate(x); // opposite alignment: double rotation
        rotate(x);
        return x;
      }
      // obs: we return the value that is the new root
    }
  }
  /*
   * VISUAL EXPLANATION:
   * STRAIGHT case
   * z
   * /
   * y
   * /
   * x
   * rotation(y)
   * y
   * / \
   * x z
   * Not it is balanced and correct BST
   * 
   * ZIG ZAG case:
   * z
   * /
   * y
   * \
   * x
   * rotation(x)
   * z
   * /
   * x
   * /
   * y
   * rotation(x)
   * x
   * / \
   * y z
   */

  // ----------- end of nested BalanceableBinaryTree class -----------

  // We reuse the LinkedBinaryTree class. A limitation here is that we only use
  // the key.
  // protected LinkedBinaryTree<Entry<K, V>> tree = new LinkedBinaryTree<Entry<K,
  // V>>();
  protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

  /**
   * Constructs an empty map using the natural ordering of keys.
   */
  public TreeMap() {
    super(); // the AbstractSortedMap constructor
    tree.addRoot(null); // create a sentinel leaf as root
  }

  /**
   * Constructs an empty map using the given comparator to order keys.
   *
   * @param comp comparator defining the order of keys in the map
   */
  public TreeMap(Comparator<K> comp) {
    super(comp); // the AbstractSortedMap constructor
    tree.addRoot(null); // create a sentinel leaf as root
  }

  /**
   * Returns the number of entries in the map.
   *
   * @return number of entries in the map
   */
  @Override
  public int size() {
    return (tree.size() - 1) / 2; // only internal nodes have entries
  }

  protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
    return tree.restructure(x);
  }

  /**
   * Rebalances the tree after an insertion of specified position. This version of
   * the method does not do anything, but it can be overridden by subclasses.
   *
   * @param p the position which was recently inserted
   */
  protected void rebalanceInsert(Position<Entry<K, V>> p) {
    // LEAVE EMPTY
  }

  /**
   * Rebalances the tree after a child of specified position has been removed.
   * This version of the method does not do anything, but it can be overridden by
   * subclasses.
   *
   * @param p the position of the sibling of the removed leaf
   */
  protected void rebalanceDelete(Position<Entry<K, V>> p) {
    // LEAVE EMPTY
  }

  /**
   * Rebalances the tree after an access of specified position. This version of
   * the method does not do anything, but it can be overridden by a subclasses.
   *
   * @param p the Position which was recently accessed (possibly a leaf)
   */
  protected void rebalanceAccess(Position<Entry<K, V>> p) {
    // LEAVE EMPTY
  }

  /**
   * Utility used when inserting a new entry at a leaf of the tree
   */
  private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
    tree.set(p, entry);
    tree.addLeft(p, null);
    tree.addRight(p, null);
  }

  // Some notational shorthands for brevity (yet not efficiency)
  protected Position<Entry<K, V>> root() {
    return tree.root();
  }

  protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) {
    return tree.parent(p);
  }

  protected Position<Entry<K, V>> left(Position<Entry<K, V>> p) {
    return tree.left(p);
  }

  protected Position<Entry<K, V>> right(Position<Entry<K, V>> p) {
    return tree.right(p);
  }

  protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) {
    return tree.sibling(p);
  }

  protected boolean isRoot(Position<Entry<K, V>> p) {
    return tree.isRoot(p);
  }

  protected boolean isExternal(Position<Entry<K, V>> p) {
    return tree.isExternal(p);
  }

  protected boolean isInternal(Position<Entry<K, V>> p) {
    return tree.isInternal(p);
  }

  protected void set(Position<Entry<K, V>> p, Entry<K, V> e) {
    tree.set(p, e);
  }

  protected Entry<K, V> remove(Position<Entry<K, V>> p) {
    return tree.remove(p);
  }

  /**
   * Returns the position in p's subtree having the given key (or else the
   * terminal leaf).
   *
   * @param key a target key
   * @param p   a position of the tree serving as root of a subtree
   * @return Position holding key, or last node reached during search
   */
  /**
   * Returns the position in p's subtree having the given key (or else the
   * terminal leaf).
   */
  private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
    if (isExternal(p))
      return p;

    int comp = compare(key, p.getElement()); // comparator between target key and current node element
    if (comp == 0) {
      return p;
    } else if (comp < 0) { // if key value is not at this node we look in subtree, recursively
      return treeSearch(left(p), key);
    } else {
      return treeSearch(right(p), key);
    }
  }

  // EXTREME IMPORTANCE: BST TREE RULE: left subtree < node < right
  // subtree!!!!!!!!!

  /**
   * Returns position with the minimal key in the subtree rooted at Position p.
   *
   * @param p a Position of the tree serving as root of a subtree
   * @return Position with minimal key in subtree
   */
  protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) { // because we have BST - see rule above
    Position<Entry<K, V>> walk = p;
    while (isInternal(left(walk))) { // while kid is not null (next level is sentinel null leaf)
      walk = left(walk);
    }
    return walk; // return the actual leaf
  }
  /*
   * OBSERVATION: WE USE SENTINEL LEAFS! (always null leafs) -> external nodes are
   * actually null, and real leafs are one level above
   */

  /**
   * Returns the position with the maximum key in the subtree rooted at p.
   *
   * @param p a Position of the tree serving as root of a subtree
   * @return Position with maximum key in subtree
   */
  protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) { // same thing as before, but for right kid
    Position<Entry<K, V>> walk = p;
    while (isInternal(right(walk))) {
      walk = right(walk);
    }
    return walk;
  }

  /**
   * Returns the value associated with the specified key, or null if no such entry
   * exists.
   *
   * @param key the key whose associated value is to be returned
   * @return the associated value, or null if no such entry exists
   */
  @Override
  public V get(K key) throws IllegalArgumentException {
    Position<Entry<K, V>> p = treeSearch(root(), key);// we search from the root for the key value
    rebalanceAccess(p); // NOT YET IMPLEMENTED, but rebalances tree *
    if (isExternal(p))
      return null;
    return p.getElement().getValue();
  }

  // IMPORTANT: rebalanceAccess(p) is called after treeSearch so that subclasses
  // (like AVL or
  // Splay trees) can rebalance the tree after access, even though in plain
  // TreeMap it does nothing.

  /**
   * Associates the given value with the given key. If an entry with the key was
   * already in the map, this replaced the previous value with the new one and
   * returns the old value. Otherwise, a new entry is added and null is returned.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with the key (or null, if no such
   *         entry)
   */

  // DIFFICULT

  @Override
  public V put(K key, V value) throws IllegalArgumentException {
    Entry<K, V> entry = new MapEntry<>(key, value); // we create new entry - RECAP THIS!
    Position<Entry<K, V>> p = treeSearch(root(), key); // we search from the root

    if (isExternal(p)) { // if p is external (null), we add new leaf
      expandExternal(p, entry);
      rebalanceInsert(p);
      return null;
    } else {
      V old = p.getElement().getValue(); // we get the old nodes value
      set(p, entry); // we update it
      rebalanceAccess(p);
      return old;
    }
  }

  // VERY DIFFICULT, RECAP!!

  /**
   * Removes the entry with the specified key, if present, and returns its
   * associated value. Otherwise does nothing and returns null.
   *
   * @param key the key whose entry is to be removed from the map
   * @return the previous value associated with the removed key, or null if no
   *         such entry exists
   */
  @Override
  public V remove(K key) throws IllegalArgumentException {
    Position<Entry<K, V>> p = treeSearch(root(), key);
    if (isExternal(p)) {
      rebalanceAccess(p);
      return null;
    } else {
      V old = p.getElement().getValue();

      if (isInternal(left(p)) && isInternal(right(p))) { // HARDEST BST DELETION CASE: node to be deleted has two
                                                         // chidren
        // If a node has two real children, you cannot just remove it directly, because
        // that would break the BST structure.
        Position<Entry<K, V>> replacement = treeMin(right(p)); // the solution is we look for the smallest node in the
                                                               // right subtree - normal successor (bigger than
                                                               // everything in left subtree, but smallest in right
                                                               // subtree)
        set(p, replacement.getElement());
        p = replacement; // After copying the replacement entry upward, we now want to physically remove
                         // the replacement node itself.
      }
      // Remember: this tree uses external sentinel nodes. - So every internal node
      // has two children positions, but some of them may be external leaves.

      // At this stage, p has at most one internal child, so at least one child must
      // be external.
      Position<Entry<K, V>> leaf = isExternal(left(p)) ? left(p) : right(p); // this is how we pick the external node
      Position<Entry<K, V>> sib = sibling(leaf);
      // This sibling is the child that will remain after deletion.
      // why do we need it? - Because after removing leaf and p, sib is the node that
      // stays connected upward.

      remove(leaf);
      remove(p);

      /*
       * Together, these two removals are the standard textbook BST deletion with
       * external sentinel nodes:
       * - delete the external leaf
       * - delete its parent internal node
       * - the sibling is spliced upward automatically by the underlying tree
       * structure
       */

      rebalanceDelete(sib);
      return old;
    }
  }

  // additional behaviors of the SortedMap interface

  /**
   * Returns the entry having the least key (or null if map is empty).
   *
   * @return entry with least key (or null if map is empty)
   */
  @Override
  public Entry<K, V> firstEntry() {
    if (isEmpty())
      return null;
    return treeMin(root()).getElement();
  }

  /**
   * Returns the entry having the greatest key (or null if map is empty).
   *
   * @return entry with greatest key (or null if map is empty)
   */
  @Override
  public Entry<K, V> lastEntry() {
    if (isEmpty())
      return null;
    return treeMax(root()).getElement();
  }

  /**
   * Returns the entry with least key greater than or equal to given key (or null
   * if no such key exists).
   *
   * @return entry with least key greater than or equal to given (or null if no
   *         such entry)
   * @throws IllegalArgumentException if the key is not compatible with the map
   */
  @Override
  public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException { // returns: the smallest key ≥ given key
    Position<Entry<K, V>> p = treeSearch(root(), key); // search for key, may not find it
    if (isInternal(p))
      return p.getElement(); // exact match

    while (!isRoot(p)) { // as long as we are not at the root
      if (p == left(parent(p))) // IF NODE FITS IN LEFT SUBTREE (if it is not TOO BIG of value to be in left
                                // subtree)
        return parent(p).getElement(); // we found the next largest value in tree
      p = parent(p); // if not, move up a level
    }
    return null;
  }

  /**
   * Returns the entry with greatest key less than or equal to given key (or null
   * if no such key exists).
   *
   * @return entry with greatest key less than or equal to given (or null if no
   *         such entry)
   * @throws IllegalArgumentException if the key is not compatible with the map
   */
  @Override
  public Entry<K, V> floorEntry(K key) throws IllegalArgumentException { // same as last one, but here we look for node
                                                                         // with value <= key value
    Position<Entry<K, V>> p = treeSearch(root(), key);
    if (isInternal(p))
      return p.getElement(); // exact match

    while (!isRoot(p)) {
      if (p == right(parent(p))) // it means that value is right for it to be in the right subtree
        return parent(p).getElement();
      p = parent(p);
    }
    return null;
  }

  /**
   * Returns the entry with greatest key strictly less than given key (or null if
   * no such key exists).
   *
   * @return entry with greatest key strictly less than given (or null if no such
   *         entry)
   * @throws IllegalArgumentException if the key is not compatible with the map
   */
  @Override
  public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException { // basically like before, but now we must
                                                                         // ensure that node value is strictly less than
                                                                         // key
    Position<Entry<K, V>> p = treeSearch(root(), key);

    if (isInternal(p)) {
      if (isInternal(left(p)))
        return treeMax(left(p)).getElement(); // if we find the key, we look for max in the left subtree of the node
                                              // that represents that key
    }

    while (!isRoot(p)) { // we go from external root upwards - THIS PATH IS RIGHT!!
      if (p == right(parent(p))) // same algorithm as before - we go one level at a time
        return parent(p).getElement();
      p = parent(p);
    }
    return null;
  }

  /**
   * Returns the entry with least key strictly greater than given key (or null if
   * no such key exists).
   *
   * @return entry with least key strictly greater than given (or null if no such
   *         entry)
   * @throws IllegalArgumentException if the key is not compatible with the map
   */
  @Override
  public Entry<K, V> higherEntry(K key) throws IllegalArgumentException { // same as one above, but for node strictly >
                                                                          // than key
    Position<Entry<K, V>> p = treeSearch(root(), key);

    if (isInternal(p)) {
      if (isInternal(right(p)))
        return treeMin(right(p)).getElement(); // if we find it, we look for the min in right subtree
    }

    while (!isRoot(p)) {
      if (p == left(parent(p)))
        return parent(p).getElement();
      p = parent(p);
    }
    return null;
  }

  // Support for iteration

  /**
   * Returns an iterable collection of all key-value entries of the map.
   *
   * @return iterable collection of the map's entries
   */
  @Override
  public Iterable<Entry<K, V>> entrySet() {
    ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
    for (Position<Entry<K, V>> p : tree.inorder()) {
      if (isInternal(p)) {
        buffer.add(p.getElement());
      }
    }
    return buffer;
  }

  public String toString() {
    return tree.toString();
  }

  /**
   * Returns an iterable containing all entries with keys in the range from
   * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
   *
   * @return iterable with keys in desired range
   * @throws IllegalArgumentException if <code>fromKey</code> or
   *                                  <code>toKey</code> is not compatible with
   *                                  the map
   */
  @Override
  public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
    ArrayList<Entry<K, V>> buffer = new ArrayList<>();
    for (Entry<K, V> e : entrySet()) {
      if (compare(e.getKey(), fromKey) >= 0 && compare(e.getKey(), toKey) < 0) {
        buffer.add(e);
      }
    }
    return buffer; // arraylist of entries in the asked range
  }

  protected void rotate(Position<Entry<K, V>> p) {
    tree.rotate(p);
  }

  // remainder of class is for debug purposes only

  /**
   * Prints textual representation of tree structure (for debug purpose only).
   */
  protected void dump() {
    dumpRecurse(root(), 0);
  }

  /**
   * This exists for debugging only
   */
  private void dumpRecurse(Position<Entry<K, V>> p, int depth) {
    String indent = (depth == 0 ? "" : String.format("%" + (2 * depth) + "s", ""));
    if (isExternal(p))
      System.out.println(indent + "leaf");
    else {
      System.out.println(indent + p.getElement());
      dumpRecurse(left(p), depth + 1);
      dumpRecurse(right(p), depth + 1);
    }
  }

  public String toBinaryTreeString() {
    BinaryTreePrinter<Entry<K, V>> btp = new BinaryTreePrinter<>(this.tree);
    return btp.print();
  }

  public static void main(String[] args) {
    TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();

    Random rnd = new Random();
    int n_max = 50;
    int n = 100;
    // rnd.ints(1, n_max).limit(n).distinct().boxed().forEach(x -> treeMap.put(x,
    // x));

    Consumer<Integer> modify = x -> {
      if (rnd.nextFloat() > 0.5)
        treeMap.put(x, 0);
      else
        treeMap.remove(x);
    };
    BinaryTreePrinter<Entry<Integer, Integer>> btp = new BinaryTreePrinter<>(treeMap.tree);
    System.out.println(btp.print());

    rnd.ints(1, n_max).limit(10000000).boxed().forEach(modify);
    System.out.println(btp.print());

    AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<Integer, Integer>();
    for (Position<Entry<Integer, Integer>> i : treeMap.tree.inorder()) {
      if (i.getElement() != null) {
        avl.put(i.getElement().getKey(), 0);
      }
    }
    System.out.println(avl.toBinaryTreeString());
  }
}
