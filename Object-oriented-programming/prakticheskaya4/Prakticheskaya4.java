package prakticheskaya4;

/**
 * Checks 2 numbers entered by a user if one is bigger than the other
 * and if they are relatively prime, validates entered values.
 */
public class Prakticheskaya4 {
  static final int MINIMAL_VALUE = -100;
  static final int MAXIMAL_VALUE = 100;
  static final int EXIT = MAXIMAL_VALUE + 1;

  /**
   * Greets a user and allows him to enter 2 numbers to check if one is bigger than
   * the other and if they are relatively prime. Shows the result to a user.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    RelativePrimeChecker relativePrimeChecker = new RelativePrimeChecker();
    Printer printer = new Printer();
    ValueValidator validator = new ValueValidator();
    NumberComparator comparator = new NumberComparator();
    int number1 = 0;
    int number2 = 0;
    String message = "";
    printer.printGreeting();
    while (true) {
      printer.printMenu();
      message = "Enter first number: ";
      number1 = validator.validateNumber(MINIMAL_VALUE, EXIT, message);
      if (number1 == EXIT) {
        System.out.println("Exiting the program");
        validator.closeScanner();
        break;
      }
      message = "Enter second number: ";
      number2 = validator.validateNumber(MINIMAL_VALUE, EXIT, message);
      if (number2 == EXIT) {
        System.out.println("Exiting the program");
        validator.closeScanner();
        break;
      }
      comparator.compareNumbers(number1, number2);
      relativePrimeChecker.checkRelativePrime(number1, number2);
    }
  }
}