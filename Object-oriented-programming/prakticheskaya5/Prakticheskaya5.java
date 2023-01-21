package prakticheskaya5;

import java.util.ArrayList;

/**
 * Interacts with the user by showing menu options and accepting input.
 * Can create, hold, print, modify and sort list of the mobile phones.
 */
public class Prakticheskaya5 {
  static final int EXIT = 0;
  static final int CREATE_UNKNOWN_PHONE = 1;
  static final int CREATE_NEW_PHONE = 2;
  static final int MODIFY_PHONE = 3;
  static final int PRINT_PHONES = 4;
  static final int SORT_PHONES = 5;
  
  /**
   * Interacts with the user by showing menu and accepting input.
   * Uses ArrayList for holding, creating, printing, modifying and sorting
   * list of mobile phones. Uses switch statements in a while loop for interacting.
   * Runs a validate functions to checks values entered by a user and catches exceptions.
   * Runs assert statement at the end of the function to make sure proper exit from the loop.
   */
  public static void main(String[] args) {
    ArrayList<MobilePhone> phonesList = new ArrayList<MobilePhone>();
    Printer printer = new Printer();
    MobilePhone phone = new MobilePhone();
    ValueValidator validator = new ValueValidator();
    PhoneModifier modifier = new PhoneModifier();
    PhoneSorter sorter = new PhoneSorter();
    MobilePhone newPhone = null;
    int userInput;
    String message;

    printer.printWelcome();
    do {
      printer.printMenu();
      message = "Enter number of a menu option: ";
      userInput = validator.validateNumber(EXIT, SORT_PHONES, message);
      switch (userInput) {
        case EXIT:
          System.out.println("Exiting the program");
          validator.closeScanner();
          break;
        case CREATE_UNKNOWN_PHONE:
          newPhone = new MobilePhone();
          phonesList.add(newPhone);
          System.out.println("New phone with unknown stats has been added to the list!");
          break;
        case CREATE_NEW_PHONE:
          newPhone = phone.createNewPhone();
          phonesList.add(newPhone);
          System.out.println("New phone has been added to the list!");
          break;
        case MODIFY_PHONE:
          try {
            modifier.modifyPhone(phonesList);
          } catch (Throwable e) {
            System.out.println("Exception occured: " + e);
            System.out.println("Initial exception: " + e.getCause());
          }
          break;
        case PRINT_PHONES:
          printer.printPhones(phonesList);
          break;
        case SORT_PHONES:
          try {
            sorter.sortPhones(phonesList);
          } catch (Throwable e) {
            System.out.println("Exception occured: " + e);
            System.out.println("Initial exception: " + e.getCause());
          }
          break;
        default:
          System.out.println("Wrong input, try again");
      }
    } while (userInput != EXIT);
    assert userInput == EXIT : "How did you exit the loop? o_O";
  }
}