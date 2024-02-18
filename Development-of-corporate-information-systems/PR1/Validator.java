package PR1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Checks that a user enters correct data by type and range.
 */
public class Validator {
    private static Scanner scanner = new Scanner(System.in);
    private static int input;

    /**
     * Checks that a user enters a number in a certain range, runs infinite loop
     * until
     * user enters correct data.
     * 
     * @param minValue Minimal value that can be accepted from a user.
     * @param maxValue Maximal value that can be accepted from a user.
     * @param message  Message that tells a user for what field the data is entered.
     * @return Succesfully validated value entered by a user.
     */
    public static int validateNumber(int minValue, int maxValue, String message) {
        while (true) {
            try {
                System.out.print(message);
                input = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("\nInput must be a number!\n");
                continue;
            } finally {
                scanner.nextLine();
            }
            if (input < minValue || input > maxValue) {
                System.out.print("\nValue must be between " + minValue + " and " + maxValue + "!\n");
                continue;
            } else {
                return input;
            }
        }
    }

    /**
     * Checks that a user enters a string and not a number.
     * 
     * @param message Message that tells a user for what field the data is entered.
     * @return Succesfully validated value entered by a user.
     */
    public static String validateString(String message) {
        while (true) {
            System.out.print(message);
            if (!scanner.hasNextDouble()) {
                return scanner.nextLine();
            } else {
                System.out.print("\nInput must be a string!\n");
                scanner.nextLine();
            }
        }
    }

    public static void closeScanner() {
        scanner.close();
    }
}