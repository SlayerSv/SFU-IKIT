package prakticheskaya2;

import java.util.ArrayList;

/**
 * Interacts with the user by showing menu options and accepting input.
 * Can create, hold, print, modify and sort list of the mobile phones.
 */
public class Prakticheskaya2 {
  static final int EXIT = 0;
  static final int CREATE_UNKNOWN_PHONE = 1;
  static final int CREATE_NEW_PHONE = 2;
  static final int MODIFY_PHONE = 3;
  static final int PRINT_PHONES = 4;
  static final int SORT_PHONES = 5;
  
  /**
   * Interacts with the user by showing menu and accepting input.
   * Uses ArrayList for holding, creating, rpinting, modifying and sorting
   * list of the mobile phones. Uses switch statements in a while loop for interacting.
   */
  public static void main(String[] args) {
    ArrayList<MobilePhone> phonesList = new ArrayList<MobilePhone>();
    Printer printer = new Printer();
    MobilePhone phone = new MobilePhone();
    ValueValidator validator = new ValueValidator();
    PhoneModifier modifier = new PhoneModifier();
    PhoneSorter sorter = new PhoneSorter();
    MobilePhone newPhone = null;
    printer.printWelcome();
    int userInput;
    do {
      printer.printMenu();
      userInput = validator.validateNumber(EXIT, SORT_PHONES);
      switch (userInput) {
        case EXIT:
          System.out.println("Exiting the program");
          validator.closeScanner();
          break;
        case CREATE_UNKNOWN_PHONE:
          newPhone = new MobilePhone();
          phonesList.add(newPhone);
          System.out.println("New phone with unknown stats has been added to the list!");
          newPhone = null;
          break;
        case CREATE_NEW_PHONE:
          newPhone = phone.createNewPhone();
          if (newPhone != null) {
            phonesList.add(newPhone);
            System.out.println("New phone has been added to the list!");
            newPhone = null;
          } else {
            System.out.println("Failed to create a phone");
          }
          break;
        case MODIFY_PHONE:
          modifier.modifyPhone(phonesList);
          break;
        case PRINT_PHONES:
          printer.printPhones(phonesList);
          break;
        case SORT_PHONES:
          sorter.sortPhones(phonesList);
          break;
        default:
          System.out.println("No such option!");
      }
    } while (userInput != EXIT);
  }
}
