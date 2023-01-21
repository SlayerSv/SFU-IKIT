package kontrolnaya;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Validator {

  public static Scanner scanner = new Scanner(System.in);

  public static int validateNumber(int minValue, int maxValue, String message) {
    int input = 0;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("\nInput must be a number!");
        continue;
      } finally {
        scanner.nextLine();
      }
      if (input < minValue || input > maxValue) {
        System.out.println("\nValue must be between " + minValue + " and "
            + maxValue + "!");
        continue;
      } else {
        return input;
      }
    }
  }

  public static double validateNumber(double minValue, double maxValue, String message) {
    double input = 0;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextDouble();
      } catch (InputMismatchException e) {
        System.out.println("\nInput must be a number!");
        continue;
      } finally {
        scanner.nextLine();
      }
      if (input < minValue || input > maxValue) {
        System.out.println("\nValue must be between " + minValue + " and " + maxValue + "!");
        continue;
      } else {
        return input;
      }
    }
  }

  public static double validateNumber(String message) {
    double input = 0;
    while (true) {
      try {
        System.out.print(message);
        input = scanner.nextDouble();
      } catch (InputMismatchException e) {
        System.out.println("\nInput must be a number!");
        continue;
      } finally {
        scanner.nextLine();
      }
      return input;
    }
  }
}