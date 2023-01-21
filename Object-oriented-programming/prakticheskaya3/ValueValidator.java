package prakticheskaya3;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Checks that a user enters correct data by type and range.
 */
public class ValueValidator {
  private Scanner scanner = new Scanner(System.in);
  private int input;

  /**
   * Checks that a user enters a number in a certain range, runs infinite loop until
   * user enters correct data.
   * @param minValue Minimal value that can be accepted from a user.
   * @param maxValue Maximal value that can be accepted from a user.
   * @param message Message that tells a user for what field the data is entered.
   * @return Succesfully validated value entered by a user.
   */
  public int validateNumber(int minValue, int maxValue, String message) {
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Input must be a number!");
        continue;
      } finally {
        scanner.nextLine();
      }
      if (input < minValue || input > maxValue) {
        System.out.println("Value must be between " + minValue + " and " + maxValue + "!");
        continue;
      } else {
        return input;
      }
    }
  }

  /**
   * Checks that a user enters a string and not a number.
   * @param message Message that tells a user for what field the data is entered.
   * @return Succesfully validated value entered by a user.
   */
  public String validateString(String message) {
    while (true) {
      System.out.print(message);
      if (!scanner.hasNextDouble()) {
        return scanner.nextLine();
      } else {
        System.out.println("Input must be a string!");
        scanner.nextLine();
      }
    }
  }

  public void closeScanner() {
    scanner.close();
  }
}