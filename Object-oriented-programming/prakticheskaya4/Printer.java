package prakticheskaya4;

/** Prints different kind of information to a user.*/
public class Printer {

  /**Prints a greeting to a user when the program starts.*/
  public void printGreeting() {
    System.out.println("");
    System.out.println("Hello, welcome to number match program!");
    System.out.println("In this program, you can check if one number is bigger than other,");
    System.out.println("or you can check if 2 numbers are relatively prime!");
  }

  /**Prints to a user what he needs to do and how to exit the program.*/
  public void printMenu() {
    System.out.println("");
    System.out.println("Enter 2 numbers to check them");
    System.out.println("Type '" + Prakticheskaya4.EXIT + "' to exit the program");
  }

  /**
   * Accepts an array of numbers and prints all numbers that an array has.
   * @param numbers Array of numbers, that will be printed to a user.
   */
  public void printArrayNumbers(int[] numbers) {
    for (int i = 0; i < numbers.length; i++) {
      if (i == (numbers.length - 1)) {
        System.out.println(numbers[i] + ".");
      } else {
        System.out.print(numbers[i] + ", ");
      }
    }
  }
}