package prakticheskaya6;

/**
 * Main class of the program, accepts input from a user and runs appropriate functions.
 * Allows a user to create and manage queues of different data types and switch between them
 * without losing data. Program runs an infinite loop showing menu and accepting an input from
 * a user to choose menu options, until user choose to exit the program.
 */
public class Prakticheskaya6 {
  static final int EXIT = 0;
  static final int IS_QUEUE_EMPTY = 1;
  static final int ADD_ELEMENT = 2;
  static final int REMOVE_ELEMENT = 3;
  static final int GET_HEAD = 4;
  static final int DUPLICATE_TAIL = 5;
  static final int FIND_AND_REMOVE_ELEMENT = 6;
  static final int PRINT_QUEUE = 7;
  static final int CLEAR_QUEUE = 8;
  static final int CHANGE_QUEUE_DATA_TYPE = 9;
  
  /**
   * Main function that interacts with a user by showing him menu options and accepting
   * input. It creates and holds queues of different data types, allowing a user to choose with
   * which data type he wants to work.  
   * 
   * <p>The queue that is currently being operated on is decided by enum data type values.
   * By changing it user can switch to a different data type queue without losing data.
   * 
   * <p>Uses infinite loop for showing user menu options and accepting input, until user
   * chooses {@code EXIT}. Uses 2 switch statements, the main one for determing which menu option
   * to run, and a second one inside it to determine which data type queue to use.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    DataType queueDataType;
    MyQueue<Integer> queueInt = new MyQueue<>();
    MyQueue<String> queueStr = new MyQueue<>();
    int menuOption;
    String message;

    Printer.printWelcome();
    queueDataType = DataType.changeDataType();
    do {
      Printer.printMenu();
      message = "\nEnter the number of a menu option: ";
      menuOption = Validator.validateInt(message, EXIT, CHANGE_QUEUE_DATA_TYPE);
      switch (menuOption) {
        case EXIT:
          System.out.println("Exiting the program");
          Validator.scanner.close();
          break;
        case IS_QUEUE_EMPTY:
          switch (queueDataType) {
            case STRING -> queueStr.emptyCheck();
            case INTEGER -> queueInt.emptyCheck();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case ADD_ELEMENT:
          message = "Enter new element: ";
          switch (queueDataType) {
            case STRING -> {
              System.out.print(message);
              queueStr.add(Validator.scanner.nextLine());
            }
            case INTEGER -> queueInt.add((Integer) Validator.validateInt(message));
            default -> System.out.println("Unsupported data type");
          }
          break;
        case REMOVE_ELEMENT:
          switch (queueDataType) {
            case STRING -> queueStr.remove();
            case INTEGER -> queueInt.remove();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case GET_HEAD:
          switch (queueDataType) {
            case STRING -> queueStr.getHead();
            case INTEGER -> queueInt.getHead();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case DUPLICATE_TAIL:
          switch (queueDataType) {
            case STRING -> queueStr.duplicateTail();
            case INTEGER -> queueInt.duplicateTail();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case FIND_AND_REMOVE_ELEMENT:
          message = "Enter the value of the element you want to remove: ";
          switch (queueDataType) {
            case STRING -> {
              System.out.println(message);
              queueStr.remove(Validator.scanner.nextLine());
            }
            case INTEGER -> queueInt.remove(Validator.validateInt(message));
            default -> System.out.println("Unsupported data type");
          }
          break;
        case PRINT_QUEUE:
          switch (queueDataType) {
            case STRING -> queueStr.printQueue();
            case INTEGER -> queueInt.printQueue();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case CLEAR_QUEUE:
          switch (queueDataType) {
            case STRING -> queueStr.clear();
            case INTEGER -> queueInt.clear();
            default -> System.out.println("Unsupported data type");
          }
          break;
        case CHANGE_QUEUE_DATA_TYPE:
          queueDataType = DataType.changeDataType();
          break;
        default:
          System.out.println("No such menu option");
      }
    } while (menuOption != EXIT);
  }
}