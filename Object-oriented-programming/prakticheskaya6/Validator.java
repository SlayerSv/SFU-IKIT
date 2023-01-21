package prakticheskaya6;

import java.util.Scanner;

/**
 * Class for validating user input to make sure that he entered correct values.
 */
public class Validator {
  
  public static Scanner scanner = new Scanner(System.in);

  /**
   * Checks that the entered value is an integer and returns if it is. Throws exceptions if not.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being an integer.
   * @throws NumberFormatException If the passed value can not to be resolved to an integer or is
   *      an empty line.
   */
  public static int validateInt(String message) {
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
        return output;
      } catch (NumberFormatException e) {
        System.out.println(e);
        System.out.println("Input must be a number!");
        continue;
      }
    }
  }

  /**
   * Checks that the entered value is an integer and returns if it is. Throws exceptions if not.
   * Checks that the int value is in a certain range.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being an integer and correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to an integer or is
   *      an empty line.
   */
  public static int validateInt(String message, int minValue, int maxValue) {
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
        System.out.println("Input must be a number!");
        continue;
      }
      if (output < minValue || output > maxValue) {
        System.out.println("Value must be between " + minValue + " and " + maxValue + "!");
        continue;
      } else {
        return output;
      }
    }
  }
}