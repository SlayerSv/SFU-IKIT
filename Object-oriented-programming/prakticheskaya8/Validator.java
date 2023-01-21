package prakticheskaya8;

import java.util.Scanner;

/**
 * Class for validating user`s input to make sure that correct values has been entered.
 */
public class Validator {

  public static Scanner scanner = new Scanner(System.in);

  /**
   * Checks that the entered value is an integer and returns if it is. Throws exceptions if not.
   * Checks that the int value is in a certain range.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being an integer and in correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to an integer or is
   *      an empty line.
   */
  public static int validateInt(int minValue, int maxValue, String message) {
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