package prakticheskaya13;

import java.util.Scanner;

/**
 * Class for validating user`s input to make sure that correct values have been entered.
 * Implements Sinlgeton pattern for creating only one object.
 */
public final class Validator {
  
  private static Validator validator;
  private static Scanner scanner;
  
  private Validator() {
    scanner = new Scanner(System.in);
  }

  /**
   * Entry point for this class, allows to get a object of this class. Only one objects can exist.
   * If the objects has already been created, then returns that object, if not then creates
   * a new one.

   * @return Singleton instance if this class.
   */
  public static synchronized Validator getValidator() {
    if (validator == null) {
      validator = new Validator();
    }
    return validator;
  }

  /**
   * Closes the {@code Scanner}.
   */
  public static void closeScanner() {
    if (scanner != null) {
      scanner.close();
    }
  }

  /**
   * Checks that the entered value is an integer and returns if it is. Throws exceptions if not.
   * Checks that the int value is in a certain range.

   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being an integer and in correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to an integer or is
   *      an empty line.
   */
  public int validateInt(int minValue, int maxValue, String message) {
    String input;
    int output;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextLine();
        if (input == "") {
          throw new NumberFormatException();
        }
        output = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println(e);
        System.out.println("Нужно ввести целое число!");
        continue;
      }
      if (output < minValue || output > maxValue) {
        System.out.println("Число должно быть между " + minValue + " и " + maxValue + "!");
        continue;
      } else {
        return output;
      }
    }
  }
}