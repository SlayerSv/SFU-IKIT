package prakticheskaya4;

/**
 * Checks if 2 numbers are relatively prime (meaning they have no common divisors).
 */
public class RelativePrimeChecker {

  /** Holds array of numbers representing common divisors of 2 numbers.*/
  private int[] sameDivisors;
  Printer printer = new Printer();

  /**
   * Accepts 2 numbers, runs match function to check if they are relatively prime
   * (meaning they have no common divisors). Displays the result to a user.
   *  If they are not relatively prime, prints common divisors of these 2 numbers.
   * @param number1 First number to compare.
   * @param number2 Second number to compare.
   */
  public void checkRelativePrime(int number1, int number2) {
    if (match.match(number1, number2)) {
      System.out.println("Numbers " + number1 + " and " + number2 + " are relatively prime!");
    } else {
      if (sameDivisors.length != 1) {
        System.out.print("Numbers " + number1 + " and " + number2 + " are not relatively prime!"
            + " Their common divisors are: ");
        printer.printArrayNumbers(sameDivisors);
      } else {
        System.out.print("Numbers " + number1 + " and " + number2 + " are not relatively prime!"
            + " Their common divisor is ");
        printer.printArrayNumbers(sameDivisors);
      }
    }
  }

  /**
   * Function from Matchable functional interface as a lambda expression,
   * accepts 2 numbers and runs functions to check if they have common divisors.
   * @param number1 First number to compare.
   * @param number2 Second number to compare.
   * @return boolean value that says if 2 numbers have common divisors.
   */
  Matchable match = (int number1, int number2) -> {
    boolean isRelativePrime = false;
    int[] divisors1 = getDivisors(number1);
    int[] divisors2 = getDivisors(number2);
    sameDivisors = getSameDivisors(divisors1, divisors2);
    if (sameDivisors.length == 0) {
      isRelativePrime = true;
    }
    return isRelativePrime;
  };
  
  /**
   * Accepts a number and return it`s divisors (division without remainder) as an array.
   * First, it calculates how many divisors the numbers has, then creates an array with that length.
   * After that fills that array with divisors of that number.
   * @param number Number that will be checked for how many divisors it has.
   * @return Array of values, representing divisors of the accepted number.
   */
  public int[] getDivisors(int number) {
    if (number < 0) {
      number = -number;
    }
    int arrayLength = 0;
    for (int i = 2; i <= number; i++) {
      if ((number % i) == 0) {
        arrayLength++;
      }
    }
    int[] divisors = new int[arrayLength];
    for (int i = 2, j = 0; i <= number; i++) {
      if ((number % i) == 0) {
        divisors[j] = i;
        j++;
        
      }
    }
    return divisors;
  }

  /**
   * Accepts 2 arrays of numbers and checks if they hold same numbers.
   * First it calculates how many numbers they have in common, then creates an
   * array with that length. After that, fills that array with the values that
   * both array contain.
   * @param divisors1 First array of numbers to compare.
   * @param divisors2 Second array of numbers to compare.
   * @return Array of values that both accepted arrays contain.
   */
  public int[] getSameDivisors(int[] divisors1, int[] divisors2) {
    int numberOfSameDivisors = 0;
    for (int i = 0; i < divisors1.length; i++) {
      for (int j = 0; j < divisors2.length; j++) {
        if (divisors1[i] == divisors2[j]) {
          numberOfSameDivisors++;
        }
      }
    }
    int[] sameDivisors = new int[numberOfSameDivisors];
    if (sameDivisors.length == 0) {
      return sameDivisors;
    }
    for (int i = 0, k = 0; i < divisors1.length; i++) {
      for (int j = 0; j < divisors2.length; j++) {
        if (divisors1[i] == divisors2[j]) {
          sameDivisors[k] = divisors1[i];
          k++;
        }
      }
    }
    return sameDivisors;
  }
}