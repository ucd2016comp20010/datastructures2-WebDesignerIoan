package project20280.exercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import project20280.interfaces.Entry;
import project20280.hashtable.ChainHashMap;

public class Wk9 {

  public static void main(String[] args) throws FileNotFoundException {
    File f = new File("sample_text.txt");
    ChainHashMap<String, Integer> counter = new ChainHashMap<>();

    Scanner scanner = new Scanner(f);

    while (scanner.hasNext()) {
      String word = scanner.next();

      // normalize
      word = word.toLowerCase().replaceAll("[^a-z]", ""); // we make sure that words such as Treat treat and treat, are
                                                          // treated the same

      if (word.isEmpty())
        continue;

      Integer count = counter.get(word); // we look for the word

      /*
       * OBSERVATION: keys can be strings too, and we use hashCode() (inside
       * hashValue() -> Abstract Hash Map) to convert the string into a usable key
       * value that identifies entry
       */

      if (count == null) {
        counter.put(word, 1);
      } else {
        counter.put(word, count + 1);
      }
    }

    scanner.close();

    // copy entries into list so we can sort them
    ArrayList<Entry<String, Integer>> entries = new ArrayList<>();
    for (Entry<String, Integer> e : counter.entrySet()) {
      entries.add(e);
    }

    // sort descending by value
    entries.sort(new Comparator<Entry<String, Integer>>() {
      @Override
      public int compare(Entry<String, Integer> a, Entry<String, Integer> b) {
        return Integer.compare(b.getValue(), a.getValue());
      }
    });

    System.out.println("Top 10 most frequent words:");
    for (int i = 0; i < Math.min(10, entries.size()); i++) {
      Entry<String, Integer> e = entries.get(i);
      System.out.println((i + 1) + ". " + e.getKey() + " -> " + e.getValue());
    }
  }
}
