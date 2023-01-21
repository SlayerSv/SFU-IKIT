package prakticheskaya5;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Checks entered data by a user to make sure it is a string or
 * a number in a certain range, throws and handles custom exceptions if not.
 */
public class ValueValidator {
  public Scanner scanner = new Scanner(System.in);

  /**
   * Checks that a user entered a string, by running hasNextDouble scanner method,
   * throws custom exception if not.
   * @message Message that will be displayed to user when entering data.
   * @return Validated string entered by a user.
   */
  public String validateString(String message) {
    while (true) {
      try {
        System.out.print(message);
        if (!scanner.hasNextDouble()) {
          String input = scanner.nextLine();
          return input;
        } else {
          scanner.nextLine();
          throw new NotAStringException(message + "THIS FIELD MUST BE A STRING!");
        }
      } catch (NotAStringException e) {
        System.out.println("Exception occured: " + e);
      }
    }
  }

  /**
   * Checks that a user entered a number (double), by running hasNextDouble scanner method.
   * Makes sure that entered value is in a certain range. Throws custom exceptions if not.
   * @param minValue Lowest acceptable value from a user.
   * @param maxValue Highest acceptable value from a user.
   * @message Message that will be displayed to user when entering data.
   * @return Validated double value entered by a user.
   */
  public double validateNumber(double minValue, double maxValue, String message) {
    double number;
    while (true) {
      try {
        System.out.print(message);
        if (scanner.hasNextDouble()) {
          number = scanner.nextDouble();
          scanner.nextLine();
        } else {
          scanner.nextLine();
          throw new NotANumberException(message + "THIS FIELD MUST BE A NUMBER!");
        }
        if (number < minValue || number > maxValue) {
          String errorMsg = "Number must be in range [" + minValue + "; " + maxValue + "]";
          throw new IncorrectNumberRangeException(message + errorMsg.toUpperCase());
        }
        return number;
      } catch (InputMismatchException | IncorrectNumberRangeException e) {
        System.out.println("Exception occured: " + e);
      }
    }
  }

  /**
   * Checks that a user entered a number (double), by running hasNextDouble scanner method.
   * Makes sure that entered value is equal or greater than a certain value. Throws custom
   * exceptions if not.
   * @param minValue Lowest acceptable value from a user.
   * @message Message that will be displayed to user when entering data.
   * @return Validated double value entered by a user.
   */
  public double validateNumber(double minValue, String message) {
    double number;
    while (true) {
      try {
        System.out.print(message);
        if (scanner.hasNextDouble()) {
          number = scanner.nextDouble();
          scanner.nextLine();
        } else {
          scanner.nextLine();
          throw new NotANumberException(message + "THIS FIELD MUST BE A NUMBER!");
        }
        if (number < minValue) {
          String errorMsg = "Value must be greater or equal to " + minValue + "!";
          throw new IncorrectNumberRangeException(message + errorMsg.toUpperCase());
        }
        return number;
      } catch (InputMismatchException | IncorrectNumberRangeException e) {
        System.out.println("Exception occured: " + e);
      }
    }
  }

  /**
   * Checks that a user entered a number (int), by running hasNextInt scanner method.
   * Makes sure that entered value is equal or greater than a certain value. Throws custom exception
   * if not.
   * @message Message that will be displayed to user when entering data.
   * @param minValue Lowest acceptable value from a user.
   * @return Validated int value entered by a user.
   */
  public int validateNumber(int minValue, String message) {
    int number;
    while (true) {
      try {
        System.out.print(message);
        if (scanner.hasNextInt()) {
          number = scanner.nextInt();
          scanner.nextLine();
        } else {
          if (scanner.hasNextDouble()) {
            scanner.nextLine();
            throw new NotAnIntegerException(message + "THIS FIELD MUST BE A WHOLE NUMBER!");
          }
          scanner.nextLine();
          throw new NotANumberException(message + "THIS FIELD MUST BE A NUMBER!");
        }
        if (number < minValue) {
          String errorMsg = "Value must be greater or equal to " + minValue + "!";
          throw new IncorrectNumberRangeException(message + errorMsg.toUpperCase());
        }
        return number;
      } catch (InputMismatchException | IncorrectNumberRangeException e) {
        System.out.println("Exception occured: " + e);
      }
    }
  }

  /**
   * Checks that a user entered a number (int), by running hasNextInt scanner method.
   * Makes sure that entered value is in a certain range. Throws custom exceptions if not.
   * @param minValue Lowest acceptable value from a user.
   * @param maxValue Highest acceptable value from a user.
   * @message Message that will be displayed to user when entering data.
   * @return Validated int value entered by a user.
   */
  public int validateNumber(int minValue, int maxValue, String message) {
    int number;
    while (true) {
      try {
        System.out.print(message);
        if (scanner.hasNextInt()) {
          number = scanner.nextInt();
          scanner.nextLine();
        } else {
          if (scanner.hasNextDouble()) {
            scanner.nextLine();
            throw new NotAnIntegerException(message + "THIS FIELD MUST BE A WHOLE NUMBER!");
          }
          scanner.nextLine();
          throw new NotANumberException(message + "THIS FIELD MUST BE A NUMBER!");
        }
        if (number < minValue || number > maxValue) {
          String errorMsg = "Number must be in range [" + minValue + "; " + maxValue + "]";
          throw new IncorrectNumberRangeException(message + errorMsg.toUpperCase());
        }
        return number;
      } catch (InputMismatchException | IncorrectNumberRangeException e) {
        System.out.println("Exception occured: " + e);
      }
    }
  }
  
  public void closeScanner() {
    scanner.close();
  }
}