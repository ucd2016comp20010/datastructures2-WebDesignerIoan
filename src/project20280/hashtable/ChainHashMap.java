package project20280.hashtable;

import project20280.interfaces.Entry;

import java.util.ArrayList;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
  // a fixed capacity array of UnsortedTableMap that serve as buckets
  private UnsortedTableMap<K, V>[] table; // initialized within createTable

  /**
   * Creates a hash table with capacity 11 and prime factor 109345121.
   */
  public ChainHashMap() {
    super();
  }

  /**
   * Creates a hash table with given capacity and prime factor 109345121.
   */
  public ChainHashMap(int cap) {
    super(cap);
  }

  /**
   * Creates a hash table with the given capacity and prime factor.
   */
  public ChainHashMap(int cap, int p) {
    super(cap, p);
  }

  /**
   * Creates an empty table having length equal to current capacity.
   */
  @Override
  @SuppressWarnings({ "unchecked" })
  protected void createTable() {
    table = new UnsortedTableMap[capacity];
  }

  /**
   * Returns value associated with key k in bucket with hash value h. If no such
   * entry exists, returns null.
   *
   * @param h the hash value of the relevant bucket
   * @param k the key of interest
   * @return associate value (or null, if no such entry)
   */
  @Override
  protected V bucketGet(int h, K k) {
    UnsortedTableMap<K, V> bucket = table[h]; // we obtain the hash table for the entry index!
    if (bucket == null) // if there is nothing in there
      return null;
    return bucket.get(k);
  }

  /**
   * Associates key k with value v in bucket with hash value h, returning the
   * previously associated value, if any.
   *
   * @param h the hash value of the relevant bucket
   * @param k the key of interest
   * @param v the value to be associated
   * @return previous value associated with k (or null, if no such entry)
   */
  @Override
  protected V bucketPut(int h, K k, V v) {
    UnsortedTableMap<K, V> bucket = table[h]; // create new bucket (instance of hash table)
    if (bucket == null) {
      bucket = new UnsortedTableMap<>();
      table[h] = bucket; // if the position related to the entry was non existent, we create it
    }

    int oldSize = bucket.size(); // compute the size of the old bucket (hash table from this index)
    V answer = bucket.put(k, v); // introduce the key and the value

    // at this point, if the key would be identical, we would replace the bucket
    // entry, and so the global size
    // (n) would not increase, but if the key is different we have officially
    // created a new entry for the bucket

    if (bucket.size() > oldSize)
      n++; // increase size - this is global size over all entries in all buckets

    return answer;
  }

  /*
   * OBSERVATION: key identifies the hash entry ONLY INSIDE THE BUCKET - outside
   * its the bucket index that identifies the buckets (derived from key somehow)
   * 
   * OBSERVATION: key must be unique!
   */

  /**
   * Removes entry having key k from bucket with hash value h, returning the
   * previously associated value, if found.
   *
   * @param h the hash value of the relevant bucket
   * @param k the key of interest
   * @return previous value associated with k (or null, if no such entry)
   */
  @Override
  protected V bucketRemove(int h, K k) {
    UnsortedTableMap<K, V> bucket = table[h];
    if (bucket == null)
      return null;

    int oldSize = bucket.size();
    V answer = bucket.remove(k);

    if (bucket.size() < oldSize) // same as before (see bucketPut)
      n--;

    return answer;
  }

  /**
   * Returns an iterable collection of all key-value entries of the map.
   *
   * @return iterable collection of the map's entries
   */
  @Override
  public Iterable<Entry<K, V>> entrySet() {
    /*
     * for each element in (UnsortedTableMap []) table
     * for each element in bucket:
     * print element
     */
    ArrayList<Entry<K, V>> entries = new ArrayList<>();
    for (UnsortedTableMap<K, V> tm : table) {
      if (tm != null) {
        for (Entry<K, V> e : tm.entrySet()) {
          entries.add(e);
        }
      }
    }
    return entries;
  }

  public String toString() {
    return entrySet().toString();
  }

  public static void main(String[] args) {
    // Question 4
    ChainHashMap<Integer, Integer> m = new ChainHashMap<>(19); // here we are able to set the capacity (nr of bucktes to
                                                               // 19)

    Integer[] keys = { 12, 44, 13, 88, 23, 94, 11, 39, 20, 16, 5 };

    for (Integer k : keys) {
      m.put(k, k); // use key as value as well, since we only care about placement
    }

    System.out.println("Entries in map: " + m);
    System.out.println("Capacity: " + m.capacity);
    System.out.println();

    System.out.println("Hash table buckets:");
    for (int i = 0; i < m.table.length; i++) {
      System.out.println(i + " -> " + m.table[i]);
    }
  }
  // Obs: when running, we will have automatic resizing because of the number of
  // entries
}
/*
 * OBSERVATION: the number of bukets (capacity) can either be provided by user
 * when initializing the AbstractHashMap, or otherwise the default one of 17
 * (defined in constructor) is used
 */
