package prakticheskaya4;

/**Checks if one number is bigger than the other, displays the result to a user.*/
public class NumberComparator {

  /**
   * Accepts 2 numbers and runs match function to check if one number is bigger than the other.
   * Displays the result to a user.
   * @param number1 Number that needs to be checked if its bigger than the other number.
   * @param number2 Second number, against which first number is checked.
   */
  public void compareNumbers(int number1, int number2) {
    if (match.match(number1, number2)) {
      System.out.println("Number " + number1 + " is bigger than " + number2 + "!");
    } else {
      System.out.println("Number " + number1 + " is not bigger than " + number2 + "!");
    }
  }

  /**
   * Implementation of a method match from functional interface Matchable, 
   * accepts 2 numbers and returns true if first number is bigger than the second,
   * returns false otherwise. Written as a lambda expression.
   * @param number1 Number that will be compared to another number.
   * @param number2 Number against which the first number will be compared.
   * @return Boolean value that says if the first number is bigger than the second.
   */
  Matchable match = (int number1, int number2) -> {
    return number1 > number2;
  };
}