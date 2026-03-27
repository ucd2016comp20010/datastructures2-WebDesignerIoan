package project20280.tree;

import project20280.interfaces.Position;

import java.util.ArrayList;

/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {

  static java.util.Random rnd = new java.util.Random();
  /**
   * The root of the binary tree
   */
  protected Node<E> root = null; // root of the tree

  // LinkedBinaryTree instance variables
  /**
   * The number of nodes in the binary tree
   */
  private int size = 0; // number of nodes in the tree

  /**
   * Constructs an empty binary tree.
   */
  public LinkedBinaryTree() {
  } // constructs an empty binary tree

  // constructor

  public static LinkedBinaryTree<Integer> makeRandom(int n) { // method to create random BST with range from 1 to given
                                                              // n
    LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
    bt.root = randomTree(null, 1, n);
    return bt;
  }

  // nonpublic utility

  /*
   * If you want to build a Binary Search Tree (BST) from numbers first to last,
   * you: Choose one number as root. | All smaller numbers go in the left subtree.
   * | All larger numbers go in the right subtree.
   */

  public static <T extends Integer> Node<T> randomTree(Node<T> parent, Integer first, Integer last) { // Builds a random
                                                                                                      // // binary
                                                                                                      // search tree
                                                                                                      // using all
                                                                                                      // integers from
                                                                                                      // first to last.

    if (first > last)
      return null;
    else {
      Integer treeSize = last - first + 1; // compute how many numbers are in the range (tree size)
      // choose random root position
      Integer leftCount = rnd.nextInt(treeSize); // randomly choose how many numbers go in the left subtree
      Integer rightCount = treeSize - leftCount - 1; // compute how many numbers go in the right subtree (-1 because we
                                                     // have root)
      Node<T> root = new Node<T>((T) ((Integer) (first + leftCount)), parent, null, null); // this creates the root
                                                                                           // number (say leftcount is 3
                                                                                           // and we start from 1, then
                                                                                           // root is 4)
      // recursively build subtree
      root.setLeft(randomTree(root, first, first + leftCount - 1)); // range is from first to (first + leftCount -1) at
                                                                    // first + leftcount we have root!
      root.setRight(randomTree(root, first + leftCount + 1, last)); // range is from first + leftCount + 1(root + 1) to
                                                                    // last
      return root; // this returns root of subtree (once recursive steps finish, this is what we
                   // are left with)
    }
  }

  // accessor methods (not already implemented in AbstractBinaryTree)

  public static void main(String[] args) {
    // Week 5 - ex 2

    LinkedBinaryTree<String> bt1 = new LinkedBinaryTree<>();
    String[] arr = { "A", "B", "C", "D", "E", null, "F", null, null, "G", "H", null, null, null, null };
    bt1.createLevelOrder(arr);
    System.out.println(bt1.toBinaryTreeString());

    // Week 5 - ex 3

    Integer[] inorder = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
    };

    Integer[] preorder = {
        18, 2, 1, 14, 13, 12, 4, 3, 9, 6, 5, 8, 7, 10, 11,
        15, 16, 17, 28, 23, 19, 22, 20, 21, 24, 27, 26, 25, 29, 30
    };

    LinkedBinaryTree<Integer> bt2 = new LinkedBinaryTree<>();

    bt2.construct(inorder, preorder);

    System.out.println(bt2.toBinaryTreeString());

    // Week 5 - ex 4

    int N = 10;
    LinkedBinaryTree<Integer> bt3 = LinkedBinaryTree.makeRandom(N); // we create new random linked binary tree of size N

    System.out.println(bt3.toBinaryTreeString());

    ArrayList<ArrayList<Integer>> paths = bt3.rootToLeafPaths(); // we obtain paths
    for (ArrayList<Integer> path : paths) {
      System.out.println(path);
    }

    // Week 6 - ex 9

    LinkedBinaryTree<String> bt4 = new LinkedBinaryTree<>();

    String[] arr2 = {
        "A", "B", "C", "D", "E", null, "F",
        null, null, "G", "H", null, null, null, null
    };

    bt4.createLevelOrder(arr2);

    System.out.println(bt4.toBinaryTreeString());
    System.out.println(bt4.leafNodesLeftToRight());

  }

  /////////////////////// WEEK 5 PRACTICAL
  ///// Question 3: INORDER PREORDER

  /*
   * Task: Reconstructing (or building) a binary tree from its preorder and
   * inorder traversals
   * 
   * Remember:
   * - Preorder: Root | Left | Right
   * - Inorder: Left | Root | Right
   * 
   * Method:
   * - One traversal (preorder) to know the order in which roots appear. Preorder
   * always tells you the next root (we will have an index variable, and go from
   * left to right through the preorder array values - this gives us current root)
   * - One traversal (inorder) to know how the tree is structured. At each step
   * (recursive), we find the root (from preorder) in the inorder array, and
   * everything to its left becomes left subtree, and everything to the right
   * becomes right subtree.
   * 
   * It’s using preorder as a root generator, and inorder as a structure guide.
   * Recursive structure is from preorder, but the subtrees sizes and nodes are
   * obtained from inorder.
   */

  private int preIndex = 0; // the index for preorder array (roots)

  public void construct(E[] inorder, E[] preorder) { // starter for recursive calls
    root = null;
    size = 0;
    preIndex = 0;
    root = constructHelper(inorder, preorder, 0, inorder.length - 1, null);
  }

  private Node<E> constructHelper(E[] inorder, E[] preorder,
      int inStart, int inEnd,
      Node<E> parent) {

    if (inStart > inEnd)
      return null; // if out of range

    // Root comes from preorder
    E rootValue = preorder[preIndex++];
    Node<E> node = createNode(rootValue, parent, null, null); // new node at current level, that has parent
    size++;

    // Find root in inorder
    int inIndex = inStart;
    while (!inorder[inIndex].equals(rootValue)) // looking for "root" value (from preorder) in inorder
      inIndex++;

    // now we found it
    // Build left subtree
    node.setLeft(
        constructHelper(inorder, preorder,
            inStart, inIndex - 1,
            node)); // node is parent, and range is start to index - 1 (inorder array)
    // OBS: this recursive call eventually receives the root of the current subtree,
    // and then attaches it to the node

    // Build right subtree
    node.setRight(
        constructHelper(inorder, preorder,
            inIndex + 1, inEnd,
            node)); // same but range is index + 1 to end

    return node; // after building a subtree, we return root of that subtree
  }

  ///// Question 4: Root-to-leaf paths

  /*
   * We explore each root to leaf path, starting from the root. For each node, we
   * add it to the arraylist (current result matrix row) and recursively explore
   * left and right subchild. When we reach a leaf, we create new "matrix" row,
   * and we "backtrack" to the previous level by removing the last added element
   * (node) from the copy of the previous result row. For each level, we continue
   * exploring the left and right subtrees. If a node does not have a child that
   * we have not yet explored, we return; right away, witch is the stopping
   * condition
   */

  public ArrayList<ArrayList<E>> rootToLeafPaths() { // main function that starts the recursive calls, return an "two
                                                     // dimensional array of arraylists"
    ArrayList<ArrayList<E>> result = new ArrayList<>(); // initially only has 1 empty row
    if (root == null)
      return result;

    ArrayList<E> current = new ArrayList<>();
    rootToLeafPathsHelper(root, current, result); // start of recursive calls
    return result;
  }

  private void rootToLeafPathsHelper(Node<E> node,
      ArrayList<E> current,
      ArrayList<ArrayList<E>> result) {
    if (node == null) // if there is no node here (we found a leaf) - stopping condition
      return;

    // choose
    current.add(node.getElement()); // we add the node (element) to the current arraylist (row of matrix)

    // if leaf: store a COPY of the current path
    if (node.getLeft() == null && node.getRight() == null) {
      result.add(new ArrayList<>(current)); // we create new row to the matrix, representing new root to new leaf
    } else {
      // explore
      if (node.getLeft() != null)
        rootToLeafPathsHelper(node.getLeft(), current, result);
      if (node.getRight() != null)
        rootToLeafPathsHelper(node.getRight(), current, result);
    }

    // un-choose (backtrack)
    current.remove(current.size() - 1); // after storing a root to leaf result in the matrix, we want to go back to the
                                        // previous node and so on until we reach a new path that will lead to a new
                                        // leaf.
  }

  /*
   * Scenario:
   * Level 0: 5 | Level 1: 1 7 | Level 2: 0
   * start: current = []
   * Call 1 → node 5: current = [5] (CALL LEFT CHILD)
   * Call 2 → node 1: current = [5,1] (CALL LEFT CHILD)
   * Call 3 → node 0: current = [5, 1, 0] (LEAF - STORE PATH) -> now this call
   * finishes | now we have to
   * look back until we find new path to leaf
   * current.remove(current.size() - 1); -> current = [5, 1] (Then it returns to
   * Call 2)
   * Back in Call 2 (node 1): Left subtree is done. | Now we try right subtree
   * (null → returns immediately). | Then Call 2 finishes.
   * current.remove(current.size() - 1); -> current = [5] (Then it returns to Call
   * 1)
   * Back in Call 1 (node 5): Now we explore right subtree (7). etc....
   */

  ///////////////////////////////////////////
  /**
   * Factory function to create a new node storing element e.
   */
  protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
    return new Node<E>(e, parent, left, right);
  }

  /**
   * Verifies that a Position belongs to the appropriate class, and is not one
   * that has been previously removed. Note that our current implementation does
   * not actually verify that the position belongs to this particular list
   * instance.
   *
   * @param p a Position (that should belong to this tree)
   * @return the underlying Node instance for the position
   * @throws IllegalArgumentException if an invalid position is detected
   */
  protected Node<E> validate(Position<E> p) throws IllegalArgumentException { // validates that a position is an actual
                                                                              // node that is not removed (we check that
                                                                              // by using convencion that removed node
                                                                              // has nod.getParent() == node)
    if (!(p instanceof Node)) // checks that position is actually a Node
      throw new IllegalArgumentException("Not valid position type");
    Node<E> node = (Node<E>) p; // safe cast
    if (node.getParent() == node) // our convention for defunct node (removed)
      throw new IllegalArgumentException("p is no longer in the tree");
    return node; // after casting to Node it is returned this way
  }

  /**
   * Returns the number of nodes in the tree.
   *
   * @return number of nodes in the tree
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns the root Position of the tree (or null if tree is empty).
   *
   * @return root Position of the tree (or null if tree is empty)
   */
  @Override
  public Position<E> root() {
    return root;
  }

  // update methods supported by this class

  /**
   * Returns the Position of p's parent (or null if p is root).
   *
   * @param p A valid Position within the tree
   * @return Position of p's parent (or null if p is root)
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public Position<E> parent(Position<E> p) throws IllegalArgumentException {
    return validate(p).getParent();
  }

  /**
   * Returns the Position of p's left child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the left child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  @Override
  public Position<E> left(Position<E> p) throws IllegalArgumentException {
    return validate(p).getLeft();
  }

  /**
   * Returns the Position of p's right child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the right child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  @Override
  public Position<E> right(Position<E> p) throws IllegalArgumentException {
    return validate(p).getRight();
  }

  /**
   * Places element e at the root of an empty tree and returns its new Position.
   *
   * @param e the new element
   * @return the Position of the new element
   * @throws IllegalStateException if the tree is not empty
   */
  public Position<E> addRoot(E e) throws IllegalStateException { // ADDED
    if (root != null)
      throw new IllegalStateException("Tree is not empty");
    root = createNode(e, null, null, null); // we create the root
    size = 1;
    return root;
  }

  public void insert(E e) { // ADDED
    if (root == null) {
      addRoot(e);
    } else {
      addRecursive(root, e);
    }
  }

  /*
   * BST = Binary Search Tree
   * A BST has the rule: left subtree values < node value < right subtree values
   * This function - inserts a new value e into a Binary Search Tree (BST).
   * => Steps: Compare e with the current node. | If smaller → go left. | If
   * larger (or equal) → go right. | Repeat until we find an empty spot.
   */
  // recursively add Nodes to binary tree in proper position
  @SuppressWarnings("unchecked")
  private Node<E> addRecursive(Node<E> p, E e) {
    int cmp = ((Comparable<E>) e).compareTo(p.getElement()); // Casts e to Comparable<E> | Compares it with the current
                                                             // node’s element.

    if (cmp < 0) { // e < p.element
      if (p.getLeft() == null) { // Left child does not exist - This is the correct place to insert
        Node<E> child = createNode(e, p, null, null);
        p.setLeft(child);
        size++;
        return child;
        // Create new node | Set its parent to p | Attach it as left child | Increase
        // size
      }
      return addRecursive(p.getLeft(), e); // Left child exists - Then we must continue searching deeper | So we repeat
                                           // the same logic in the left subtree.
    } else { // If e is Greater or Equal → Go Right
      if (p.getRight() == null) { // Right child does not exist - This is the correct place to insert
        Node<E> child = createNode(e, p, null, null);
        p.setRight(child);
        size++;
        return child;
      }
      return addRecursive(p.getRight(), e);// Right child exists - Then we must continue searching deeper | So we repeat
      // the same logic in the right subtree.
    }
  }

  /**
   * Creates a new left child of Position p storing element e and returns its
   * Position.
   *
   * @param p the Position to the left of which the new element is inserted
   * @param e the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p already has a left child
   */
  public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException { // ADDED
    Node<E> parent = validate(p);
    if (parent.getLeft() != null)
      throw new IllegalArgumentException("p already has a left child");
    Node<E> child = createNode(e, parent, null, null);
    parent.setLeft(child);
    size++;
    return child;
  }

  /**
   * Creates a new right child of Position p storing element e and returns its
   * Position.
   *
   * @param p the Position to the right of which the new element is inserted
   * @param e the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p already has a right child
   */
  public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException { // ADDED
    Node<E> parent = validate(p);
    if (parent.getRight() != null)
      throw new IllegalArgumentException("p already has a right child");
    Node<E> child = createNode(e, parent, null, null);
    parent.setRight(child);
    size++;
    return child;
  }

  /**
   * Replaces the element at Position p with element e and returns the replaced
   * element.
   *
   * @param p the relevant Position
   * @param e the new element
   * @return the replaced element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  public E set(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> node = validate(p); // we validate position p (and because of the validation method also cast it to
                                // Node)
    E old = node.getElement(); // Element of old node at this position
    node.setElement(e); // we add value of element e to this node
    return old; // we return old element value
  }

  /**
   * Attaches trees t1 and t2, respectively, as the left and right subtree of the
   * leaf Position p. As a side effect, t1 and t2 are set to empty trees.
   *
   * @param p  a leaf of the tree
   * @param t1 an independent tree whose structure becomes the left child of p
   * @param t2 an independent tree whose structure becomes the right child of p
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p is not a leaf
   */
  public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException {
    Node<E> node = validate(p); // we validate and cast position p to Node
    if (numChildren(node) > 0) // if not Leaf
      throw new IllegalArgumentException("p must be a leaf");

    // attach t1 as left subtree
    if (t1 != null && !t1.isEmpty()) {
      t1.root.setParent(node); // we set the new parent (Leaf) for the tree root
      node.setLeft(t1.root); // node now points (left kid) to previous tree root (pointer) -> we have
                             // officialy connected them
      size += t1.size; // allowed: same class can access private fields - we increase the total number
                       // of nodes
      // OBS: now we have two things that point to same nodes (previous leaf and top
      // of tree) - we must "delete" tree
      t1.root = null; // now t1 has no root - t1.isEmpty() returns true
      t1.size = 0;
    }

    // attach t2 as right subtree - same process
    if (t2 != null && !t2.isEmpty()) {
      t2.root.setParent(node);
      node.setRight(t2.root);
      size += t2.size;
      t2.root = null;
      t2.size = 0;
    }
  }

  /**
   * Removes the node at Position p and replaces it with its child, if any.
   *
   * @param p the relevant Position
   * @return element that was removed
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p has two children.
   */
  public E remove(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p); // validating that position p is a Node and so casting

    if (numChildren(node) == 2) // we must have either 0 or 1 child (to replace node with child)
      throw new IllegalArgumentException("p has two children");

    Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight()); // if child exists, we create the node
                                                                                 // child that receives its pointer

    if (child != null) // if child exists
      child.setParent(node.getParent()); // the new parent of this child becomes the parent of the node

    if (node == root) { // if node is root, then child becomes root
      root = child;
    } else {
      Node<E> parent = node.getParent(); // we create a new node representing the parent
      if (node == parent.getLeft()) // we search for child of parent - at this point parent still points to our
                                    // previous node
        parent.setLeft(child); // we change it so that the parent now points to the child
      else
        parent.setRight(child);
    }

    size--; // we removed node
    E old = node.getElement();

    // mark node as defunct
    node.setElement(null);
    node.setLeft(null);
    node.setRight(null);
    node.setParent(node); // this is the convention

    return old; // we return the element of the removed node
  }

  public String toString() {
    return positions().toString();
  }

  // OBS: Level order = breadth-first order -> Example array: { "A", "B", "C",
  // "D", "E", null, "F" } (we call this l)
  // Level 0: A ; Level 1: B and C; Level 2: D and E AND null and F
  // KEY RULE - for node with index i, its children are: left child → 2*i + 1 |
  // right child → 2*i + 2

  // ARRAY LIST IMPLEMENTATION ArrayList<E>
  public void createLevelOrder(ArrayList<E> l) { // Clears the tree | Builds a new tree using helper recursion |
                                                 // Interprets the list as level-order
    root = null;
    size = 0;
    // now we have completed "the reset" - no more nodes connections
    root = createLevelOrderHelper(l, null, 0); // Build subtree starting at index 0 (root of array).
  }

  private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> p, int i) { // This builds the subtree rooted
                                                                                       // at index i.
    if (i >= l.size()) // Stop if index is outside array
      return null;
    E val = l.get(i); // we get the element at the specified index from the array
    if (val == null) // Stop if element is null
      return null;

    Node<E> node = createNode(val, p, null, null); // This creates a node for this index.
    size++;

    // Recursively Build Children - This builds left and right subtrees recursively
    node.setLeft(createLevelOrderHelper(l, node, 2 * i + 1)); // continue with the build of the subtree for index
                                                              // provided by formula
    node.setRight(createLevelOrderHelper(l, node, 2 * i + 2));
    return node;
  }

  // PLAIN ARRAY E[] IMPLEMENTATION
  public void createLevelOrder(E[] arr) {
    root = null;
    size = 0;
    root = createLevelOrderHelper(arr, null, 0);
  }

  private Node<E> createLevelOrderHelper(E[] arr, Node<E> p, int i) {
    if (i >= arr.length)
      return null;
    if (arr[i] == null)
      return null;

    Node<E> node = createNode(arr[i], p, null, null);
    size++;

    node.setLeft(createLevelOrderHelper(arr, node, 2 * i + 1));
    node.setRight(createLevelOrderHelper(arr, node, 2 * i + 2));
    return node;
  }

  public String toBinaryTreeString() {
    BinaryTreePrinter<E> btp = new BinaryTreePrinter<>(this);
    return btp.print();
  }

  /**
   * Nested static class for a binary tree node.
   */
  public static class Node<E> implements Position<E> {
    private E element;
    private Node<E> left, right, parent;

    public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
      element = e;
      left = l;
      right = r;
      parent = p;
    }

    // accessor
    public E getElement() {
      return element;
    }

    // modifiers
    public void setElement(E e) {
      element = e;
    }

    public Node<E> getLeft() {
      return left;
    }

    public void setLeft(Node<E> n) {
      left = n;
    }

    public Node<E> getRight() {
      return right;
    }

    public void setRight(Node<E> n) {
      right = n;
    }

    public Node<E> getParent() {
      return parent;
    }

    public void setParent(Node<E> n) {
      parent = n;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (element == null) {
        sb.append("\u29B0");
      } else {
        sb.append(element);
      }
      return sb.toString();
    }
  }

  // Week 6: Question 9 - all leafs from left to right

  public ArrayList<E> leafNodesLeftToRight() { // main recursive function
    ArrayList<E> leaves = new ArrayList<>(); // new empty array list for leafs
    leafNodesLeftToRightHelper(root, leaves); // recursive call start (from root)
    return leaves;
  }

  private void leafNodesLeftToRightHelper(Node<E> node, ArrayList<E> leaves) { // recursive logic
    if (node == null) { // base case
      return;
    }

    leafNodesLeftToRightHelper(node.getLeft(), leaves); // traversing the left subtree

    if (node.getLeft() == null && node.getRight() == null) { // if we find leaf we add it to the array
      leaves.add(node.getElement());
    }

    leafNodesLeftToRightHelper(node.getRight(), leaves); // traversing the right subtree
  }
  // this will go through the left subtree first, then attempt to go through the
  // right one. At each level, if the recursive call stops (we found leaf), we
  // will still have other higher level recursive calls to compute
}
