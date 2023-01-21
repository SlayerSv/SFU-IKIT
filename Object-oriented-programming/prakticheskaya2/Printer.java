package prakticheskaya2;

import java.util.ArrayList;

/**
 * Prints different information to a user.
 */
public class Printer {
  /**
   * Prints a greeting to a user and shows basic info about the program.
   */
  public void printWelcome() {
    System.out.println("Hello, welcome to the Mobile Phone Handler program!");
    System.out.println("In this program you can create, modify and sort mobile phones!");
    System.out.println("To choose an option enter it`s number in the input field.");
  }

  /**
   * Prints main menu of the program.
   */
  public void printMenu() {
    System.out.println("");
    System.out.println("Choose what you want to do:");
    System.out.println("1. Create a new phone with unknown stats");
    System.out.println("2. Create a new phone with known stats");
    System.out.println("3. Modify a phone");
    System.out.println("4. Print information about all phones");
    System.out.println("5. Sort phones based on stats");
    System.out.println("");
    System.out.println("0. Exit the program");
    System.out.println("");
    System.out.print("Enter number of a menu option: ");
  }

  /**
   * Prints a numerical list of phones with all their stats.
   * @param phones ArrayList of phones that will be printed.
   */
  public void printPhones(ArrayList<MobilePhone> phones) {
    if (phones.size() == 0) {
      System.out.println("The phone list is empty!");
      return;
    }
    for (int i = 0; i < phones.size(); i++) {
      System.out.println("");
      System.out.println("Phone number " + (i + 1));
      phones.get(i).printInfo();
    }
  }

  /**
   * Prints numerated phone`s fields to a user.
   */
  public void printPhoneFields() {
    System.out.println("");
    System.out.println("1. Name");
    System.out.println("2. Price");
    System.out.println("3. Screen size");
    System.out.println("4. Memory");
    System.out.println("5. Color");
    System.out.println("");
    System.out.println("0. Cancel and go back");
    System.out.println("");
  }
}