package prakticheskaya6;

/** Class for printing information to the user. */
public class Printer {

  /** Prints greeting and short overview of the program to the user at the start of the program. */
  public static void printWelcome() {
    System.out.println("Hello, welcome to the Queue Manager App!");
    System.out.println("In this app you can create and manage your own queue of elements.");
    System.out.println("Queue can hold either strings or integers.");
    System.out.println("You can switch between different data type queues without losing data.");
    System.out.println("You can add, delete or print elements in the queue.");
    System.out.println("To start, you need to choose a type of data for the queue.");
  }

  /**Prints menu options to the user so he can choose what to pick. */
  public static void printMenu() {
    System.out.println("\nChoose what you want to do with queue:");
    System.out.println("1. Check if the queue is empty");
    System.out.println("2. Add a new element to the tail of the queue");
    System.out.println("3. Remove an element from the head of the queue");
    System.out.println("4. Display element at the head of the queue");
    System.out.println("5. Duplicate the tail of the queue");
    System.out.println("6. Remove a certain element from the queue");
    System.out.println("7. Print queue");
    System.out.println("8. Clear the queue");
    System.out.println("\n9. Change the data type of the queue (current data will be saved)");
    System.out.println("0. Exit the program");
  }
}