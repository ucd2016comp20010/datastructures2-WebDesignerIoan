package project20280.exercises;

import java.util.Arrays;

public class Wk6 {

  static long calls = 0;
  static long[] memo; // this is for the memorise version - keep the value computed once for
                      // n=something

  // Classic Fibbonaci recursive function
  public static long fib(int n) {
    calls++;
    if (n == 0)
      return 0; // base case 1
    if (n == 1)
      return 1; // base case 2
    return fib(n - 1) + fib(n - 2); // recursive step
  }

  /*
   * Memoisation stores previously computed values so each Fibonacci number is
   * computed only once. That makes the runtime O(n) instead of exponential
   */
  public static long fibMemo(int n) {
    calls++;
    if (n == 0)
      return 0;
    if (n == 1)
      return 1;

    if (memo[n] != -1) // if value for index n has already been computed before
      return memo[n]; // we return the value in the memo array

    memo[n] = fibMemo(n - 1) + fibMemo(n - 2); // else we compute (recursively) the fibbonaci value, and store it in the
                                               // array
    return memo[n];
  }

  // Question 3: Tribonacci
  public static long tribonacci(int n) {
    calls++;
    // base cases
    if (n == 0)
      return 0;
    if (n == 1)
      return 0;
    if (n == 2)
      return 1;

    return tribonacci(n - 1)
        + tribonacci(n - 2)
        + tribonacci(n - 3); // recursive case
  }

  // Question 4: nested recursive function - McCarthy 91 function (always 91 if
  // n<101 - see paper explanation)
  public static int M(int n) {
    if (n > 100) {
      return n - 10;
    } else {
      return M(M(n + 11));
    }
  }

  public static void main(String[] args) {
    // Classic fibbonaci
    int n1 = 5;
    calls = 0;
    long ans1 = fib(n1);
    System.out.println("fib(" + n1 + ") = " + ans1);
    System.out.println("recursive calls = " + calls);

    // Memo fibbonaci
    int n2 = 50;
    calls = 0;
    memo = new long[n2 + 1];
    Arrays.fill(memo, -1);

    long ans2 = fibMemo(n2);
    System.out.println("fib(" + n2 + ") = " + ans2);
    System.out.println("calls = " + calls);

    // Tribonacci
    int n3 = 9;
    calls = 0;
    long ans = tribonacci(n3);

    System.out.println("Tribonacci(" + n3 + ") = " + ans);
    System.out.println("calls = " + calls);

    // Nested recursive McCarthy
    System.out.println(M(87));
  }

}
