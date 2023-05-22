package prakticheskaya13;

/**
 * Main class of the program, contains {@code main()} method and serves as an entry point.
 * Responsible for interactiong with a user by showing him menu of the program and accepting input.
 */
public class Prakticheskaya13 {
  static final int EXIT = 0;
  static final int READ_ARTICLE = 1;
  static final int READ_TEXT = 2;
  static final int COMPLETE_TEST = 3;
  
  /**
   * Interacts with the user by showing him the menu of the program and accepting input.
   * User can choose between reading texts, articles and completing tests. 
   * Runs a switch statement in a while loop, until user chooses {@code EXIT}.
   */
  public static void main(String[] args) {
    int menuOption = 0;
    String message = "";

    Printer.printWelcome();
    do {
      Printer.printMenu();
      message = "\nВведите номер опции меню: ";
      menuOption = Validator.getValidator().validateInt(EXIT, COMPLETE_TEST, message);
      switch (menuOption) {
        case EXIT:
          System.out.println("\nПрограмма завершает работу.");
          Validator.closeScanner();
          break;
        case READ_ARTICLE:
          Controller.loadAndRunArticle();
          break;
        case READ_TEXT:
          Controller.loadAndRunText();
          break;
        case COMPLETE_TEST:;
          Controller.loadAndRunTest();
          break;
        default:
          System.out.println("\nТакой опции нет!");
      }
    } while (menuOption != EXIT);
  }
}