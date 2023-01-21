package prakticheskaya7;

/**
 * Class for displaying information to a user.
 */
public class Printer {
  /**
   * Prints a greeting to a user and shows basic info about the program.
   */
  public static void printWelcome() {
    System.out.println("Hello, welcome to the Mobile Phone Handler program!");
    System.out.println("In this program you can create, modify and sort mobile phones!");
    System.out.println("To choose an option enter it`s number in the input field.");
  }

  /**
   * Prints main menu of the program.
   */
  public static void printMenu() {
    System.out.println("");
    System.out.println("Choose what you want to do:");
    System.out.println("1. Load a list of phones");
    System.out.println("2. Print list of phones");
    System.out.println("3. Filter phones by price");
    System.out.println("4. Print only unique phones (no duplicates)");
    System.out.println("5. Print all premium phones");
    System.out.println("6. Create and add a new phone to the list");
    System.out.println("");
    System.out.println("0. Exit the program");
  }
}