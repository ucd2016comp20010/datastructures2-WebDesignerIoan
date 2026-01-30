package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            this.element = e; // reference (final) to the first node in list
            this.next = n; // reference to next node (null if it is last)
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            if(this.next!=null){
              return this.next;
            }
            return null;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            this.next=n; // we assign the reference to the next node the node n (pointer to it)
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        int count = 0;
        Node<E> current = head;
    
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
        // OBS: we use counter here because size will be updated when adding or taking out nodes (in other methods) - and should be accessed as return size;
    }

    //@Override
    public boolean isEmpty() {
        return head==null; // we check if the list is empty (first node null)
    }

    @Override
    public E get(int position) {
        int count=0;
        Node<E> current = head;
        if(position<0 || position>=size()){
          throw new IndexOutOfBoundsException("Invalid index: " + position); // we should throw exceptions instead of null!
        }

        while(count!=position){
          count++;
          current = current.getNext();
        }
        return current.getElement(); // when we want to access an element, we should always use the accessor method
    }

    @Override
    public void add(int position, E e) {
        
        if(position<0 || position>size()){ // we can also add a node at the end (after the last one)
          throw new IndexOutOfBoundsException("Invalid index: " + position); // we should throw exceptions instead of null!
        }

        if(position == 0){ // Case 1: we have to add at the beginning
          head = new Node<>(e,head);
        }else{
          Node<E> current = head;

          for (int i = 0; i < position - 1; i++) { // to add a node at position n, we must go until n-1
            current = current.getNext();
        }
          Node<E> newNode = new Node<>(e, current.getNext()); // we have to actually create a new node - this points to the next reference of the current node
          current.setNext(newNode); // the current node now points to the new added node
        }
        
        size++;
    }


    @Override
    public void addFirst(E e) {
      head = new Node<>(e, head); // new head now points to the reference of the old head
      size++;
    }

    @Override
    public void addLast(E e) {
      Node<E> newNode = new Node<>(e, null); // we actually create the new node, which must point to null (as it will become the last node in the list)

      // Case 1: empty list
      if (head == null) {
        head = newNode;
      }
      // Case 2: non-empty list
      else {
        Node<E> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        current.setNext(newNode); // we update the reference of the last node, from null to the new node
    }

    size++;
  }

    @Override
    public E remove(int position) {
        
      if(position<0 || position>=size()){
        throw new IndexOutOfBoundsException("Invalid index: " + position); // we should throw exceptions instead of null!
      }

      E removed; // we create the element to be removed

    // Case 1: remove head
    if (position == 0) {
        removed = head.getElement(); // first node
        head = head.getNext();
    }
    // Case 2: remove after head
    else {
        Node<E> current = head;

        // move to node at index position-1
        for (int i = 0; i < position - 1; i++) {
            current = current.getNext();
        } // after this we will have the reference to the "position-1" node

        Node<E> nodeToRemove = current.getNext(); // nodeToRemove is the reference to the "position" node
        removed = nodeToRemove.getElement(); // we need this because we have to return the element that is from the node to be removed

        // bypass the removed node
        current.setNext(nodeToRemove.getNext()); // now current points to the position+1 node
    }

    size--;
    return removed;

    }

    @Override
    public E removeFirst() {

      if (head == null) {
        throw new IndexOutOfBoundsException("List is empty");
      }

      E removed = head.getElement(); // save element
      head = head.getNext();         // move head
      size--;
  
      return removed; // return removed (saved) element
    }

    @Override // UNDERSTAND THIS ONE!!!!
  public E removeLast() {
    if (head == null) {
        throw new IndexOutOfBoundsException("List is empty");
    }

    // Case 1: only one element
    if (head.getNext() == null) {
        E removed = head.getElement();
        head = null;
        size--;
        return removed;
    }

    // Case 2: 2+ elements: stop at second-last node
    Node<E> current = head;
    while (current.getNext().getNext() != null) {
        current = current.getNext();
    }

    // current is now the second-last node
    E removed = current.getNext().getElement();
    current.setNext(null);   // drop the last node
    size--;

    return removed;
}


    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

    }
}


// n - reference to the current node
// n.next - reference to the next node
// n.element - reference to the DATA stored in the node
