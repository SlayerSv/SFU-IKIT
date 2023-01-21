package prakticheskaya7;

import java.util.Scanner;

/**
 * Class for validating user`s input to make sure that correct values has been entered.
 */
public class Validator {
  
  public static Scanner scanner = new Scanner(System.in);

  /**
   * Checks that a user entered a string, by running hasNextDouble scanner method,
   * throws custom exception if not.
   * @message Message that will be displayed to user when entering data.
   * @return Validated string entered by a user.
   */
  public static String validateString(String message) {
    while (true) {
      System.out.print(message);
      if (!scanner.hasNextDouble()) {
        String input = scanner.nextLine();
        return input;
      } else {
        scanner.nextLine();
        System.out.println("Input must be a string!");
        continue;
      }
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

  /**
   * Checks that the entered value is an integer and returns if it is. Throws exceptions if not.
   * Checks that the int value is in a certain range.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being an integer and in correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to an integer or is
   *      an empty line.
   */
  public static int validateInt(int minValue, String message) {
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
      if (output < minValue) {
        System.out.println("Value must be greater or equal to " + minValue + "!");
        continue;
      } else {
        return output;
      }
    }
  }

  /**
   * Checks that the entered value is a double and returns if it is. Throws exceptions if not.
   * Checks that the value is in a certain range.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being a double and in correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to a double or is
   *      an empty line.
   */
  public static double validateDouble(double minValue, double maxValue, String message) {
    String input;
    double output;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextLine();
        if (input == "") {
          throw new NumberFormatException();
        }
        output = Double.parseDouble(input);
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

  /**
   * Checks that the entered value is a double and returns if it is. Throws exceptions if not.
   * Checks that the value is in a certain range.
   * @param message Message that will be displayed to the user before the input, so he knows
   *      what value is expected of him.
   * @return User input that passed the validation for being a double and in correct range.
   * @throws NumberFormatException If the passed value can not to be resolved to a double or is
   *      an empty line.
   */
  public static double validateDouble(double minValue, String message) {
    String input;
    double output;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextLine();
        if (input == "") {
          throw new NumberFormatException();
        }
        output = Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println(e);
        System.out.println("Input must be a number!");
        continue;
      }
      if (output < minValue) {
        System.out.println("Value must be greater or equal to " + minValue + "!");
        continue;
      } else {
        return output;
      }
    }
  }
}