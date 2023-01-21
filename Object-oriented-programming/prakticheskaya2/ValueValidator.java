package prakticheskaya2;

import java.util.Scanner;

/**
 * Validates entered data by a user. Can validate string and numbers and make sure
 * that entered numbers are in certain ranges.
 */
public class ValueValidator {
  private Scanner scanner = new Scanner(System.in);

  /**
   * Validates that a user entered a string, by running hasNextDouble scanner method.
   * @return Validated string entered by a user.
   */
  public String validateString() {
    while (true) {
      if (!scanner.hasNextDouble()) {
        String input = scanner.nextLine();
        return input;
      } else {
        System.out.println("You must enter a string!");
        scanner.nextLine();
      }
    }
  }

  /**
   * Validates that a user entered a number (double), by running hasNextDouble scanner method.
   * Makes sure that entered value is in a certain range.
   * @param minValue Lowest acceptable value from a user.
   * @param maxValue Highest acceptable value from a user.
   * @return Validated double value entered by a user.
   */
  public double validateNumber(double minValue, double maxValue) {
    while (true) {
      if (scanner.hasNextDouble()) {
        double input = scanner.nextDouble();
        scanner.nextLine();
        if (input < minValue || input > maxValue) {
          System.out.println("Value must be between " + minValue + " and " + maxValue + "!");
          continue;
        }
        return input;
      } else {
        System.out.println("You must enter a number!");
        scanner.nextLine();
      }
    }
  }

  /**
   * Validates that a user entered a number (double), by running hasNextDouble scanner method.
   * Makes sure that entered value is equal or greater than a certain value.
   * @param minValue Lowest acceptable value from a user.
   * @return Validated double value entered by a user.
   */
  public double validateNumber(double minValue) {
    while (true) {
      if (scanner.hasNextDouble()) {
        double input = scanner.nextDouble();
        scanner.nextLine();
        if (input < minValue) {
          System.out.println("Value must be greater or equal to " + minValue + "!");
          continue;
        }
        return input;
      } else {
        System.out.println("You must enter a number!");
        scanner.nextLine();
      }
    }
  }

  /**
   * Validates that a user entered a number (int), by running hasNextInt scanner method.
   * Makes sure that entered value is equal or greater than a certain value.
   * @param minValue Lowest acceptable value from a user.
   * @return Validated int value entered by a user.
   */
  public int validateNumber(int minValue) {
    while (true) {
      if (scanner.hasNextInt()) {
        int input = scanner.nextInt();
        scanner.nextLine();
        if (input < 0) {
          System.out.println("Value must be greater or equal to " + minValue + "!");
          continue;
        }
        return input;
      } else {
        System.out.println("You must enter a number!");
        scanner.nextLine();
      }
    }
  }

  /**
   * Validates that a user entered a number (int), by running hasNextInt scanner method.
   * Makes sure that entered value is in a certain range.
   * @param minValue Lowest acceptable value from a user.
   * @param maxValue Highest acceptable value from a user.
   * @return Validated int value entered by a user.
   */
  public int validateNumber(int minValue, int maxValue) {
    while (true) {
      if (scanner.hasNextInt()) {
        int input = scanner.nextInt();
        scanner.nextLine();
        if (input < minValue || input > maxValue) {
          System.out.println("Value must be between " + minValue + " and " + maxValue + "!");
          continue;
        }
        return input;
      } else {
        System.out.println("You must enter a number!");
        scanner.nextLine();
      }
    }
  }

  public void closeScanner() {
    scanner.close();
  }
}