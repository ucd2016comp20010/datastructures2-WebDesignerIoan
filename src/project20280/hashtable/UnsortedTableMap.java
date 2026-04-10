package project20280.hashtable;

import project20280.interfaces.AbstractMap;
import project20280.interfaces.Entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of a map using an unsorted table.
 */

public class UnsortedTableMap<K, V> extends AbstractMap<K, V> {
  /**
   * Underlying storage for the map of entries.
   */
  private final ArrayList<MapEntry<K, V>> table = new ArrayList<>();

  /**
   * Constructs an initially empty map.
   */
  public UnsortedTableMap() {
  }

  // private utility

  /**
   * Returns the index of an entry with equal key, or -1 if none found.
   */
  private int findIndex(K key) {
    for (int i = 0; i < table.size(); i++) { // go through the list values
      if (table.get(i).getKey().equals(key)) {
        return i; // return the index if found
      }
    }
    return -1; // key not found
  }

  // public methods

  /**
   * Returns the number of entries in the map.
   *
   * @return number of entries in the map
   */
  @Override
  public int size() {
    return table.size();
  }

  /**
   * Returns the value associated with the specified key, or null if no such entry
   * exists.
   *
   * @param key the key whose associated value is to be returned
   * @return the associated value, or null if no such entry exists
   */
  @Override
  public V get(K key) {
    int index = findIndex(key); // we apply the key finding method we implemented
    if (index != -1) { // check that key was found
      return table.get(index).getValue();
    }
    return null; // key not found
  }

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
  @Override
  public V put(K key, V value) {
    int index = findIndex(key); // we first search for the key, if it exists we will change the value attributed
                                // to it
    if (index != -1) {
      V oldValue = table.get(index).getValue();
      table.get(index).setValue(value); // Update the value
      return oldValue; // return the old value
    }
    table.add(new MapEntry<>(key, value)); // Insert a new entry - at end (doesn't matter here)
    return null;
  }

  /**
   * Removes the entry with the specified key, if present, and returns its value.
   * Otherwise does nothing and returns null.
   *
   * @param key the key whose entry is to be removed from the map
   * @return the previous value associated with the removed key, or null if no
   *         such entry exists
   */
  @Override
  public V remove(K key) {
    int index = findIndex(key); // we look for the key
    if (index != -1) { // if found
      V value = table.get(index).getValue();
      table.remove(index); // Remove the entry
      return value;
    }
    return null; // key not found
  }

  // ---------------- nested EntryIterator class ----------------
  private class EntryIterator implements Iterator<Entry<K, V>> {
    private int j = 0;

    public boolean hasNext() {
      return j < table.size();
    }

    public Entry<K, V> next() {
      if (j == table.size())
        throw new NoSuchElementException("No further entries");
      return table.get(j++);
    }

    public void remove() {
      throw new UnsupportedOperationException("remove not supported");
    }
  } // ----------- end of nested EntryIterator class -----------

  // ---------------- nested EntryIterable class ----------------
  private class EntryIterable implements Iterable<Entry<K, V>> {
    public Iterator<Entry<K, V>> iterator() {
      return new EntryIterator();
    }
  } // ----------- end of nested EntryIterable class -----------

  /**
   * Returns an iterable collection of all key-value entries of the map.
   *
   * @return iterable collection of the map's entries
   */
  @Override
  public Iterable<Entry<K, V>> entrySet() {
    return new EntryIterable();
  }

  public String toString() {
    return table.toString();
  }

  public static void main(String[] args) { // main added for testing purposes
    UnsortedTableMap<String, Integer> map = new UnsortedTableMap<>();

    map.put("apple", 10);
    map.put("banana", 5);
    map.put("orange", 8);

    System.out.println("Apple value: " + map.get("apple"));
    System.out.println("Removing Banana: " + map.remove("banana"));

    // Print all keys
    System.out.print("Keyset: ");
    /*
     * IMPORTANT OBSERVATION: the for loops implicitly call the hasNext() and next()
     * methods for the iterable key and value subclasses
     */
    for (String key : map.keySet()) {
      System.out.print(key + " "); // Print each key
    }
    System.out.println(); // Move to a new line

    // Print all values
    System.out.print("Values: ");
    for (Integer value : map.values()) {
      System.out.print(value + " "); // Print each value
    }
    System.out.println(); // Move to a new line
  }
}
