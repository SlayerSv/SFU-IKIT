package prakticheskaya7;

/**
 * Main class of the program, contains {@code main()} method and serves as an entry point.
 * Responsible for interactiong with a user by showing him menu of the program and accepting input.
 * Program allows a user to get, hold, print and modify a list of mobile phones.
 */
public class Prakticheskaya7 {
  static final int EXIT = 0;
  static final int LOAD_PHONES = 1;
  static final int PRINT_ALL_PHONES = 2;
  static final int FILTER_PHONES = 3;
  static final int REMOVE_DUPLICATES = 4;
  static final int PRINT_PREMIUM_PHONES = 5;
  static final int ADD_PHONE = 6;
  
  /**
   * Interacts with the user by showing him the menu of the program and accepting input.
   * Uses {@code ListOfPhones} object to manage the list of mobile phones. 
   * Runs a switch statement in a while loop, until user chooses {@code EXIT}.
   */
  public static void main(String[] args) {
    int menuOption = 0;
    String message = "";
    ListOfPhones list = new ListOfPhones();

    Printer.printWelcome();
    do {
      Printer.printMenu();
      message = "\nEnter menu option number: ";
      menuOption = Validator.validateInt(EXIT, ADD_PHONE, message);
      switch (menuOption) {
        case EXIT:
          System.out.println("\nExiting the program");
          Validator.scanner.close();
          break;
        case LOAD_PHONES:
          list.loadPhones();
          break;
        case PRINT_ALL_PHONES:
          list.printPhones();
          break;
        case FILTER_PHONES:
          list.filterByPrice();
          break;
        case REMOVE_DUPLICATES:
          list.printUniquePhones();
          break;
        case PRINT_PREMIUM_PHONES:
          list.getPremiumPhones();
          break;
        case ADD_PHONE:
          list.addNewPhone();
          break;
        default:
          System.out.println("\nNo such option!");
      }
    } while (menuOption != EXIT);
  }
}