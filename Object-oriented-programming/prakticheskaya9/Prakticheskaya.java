package prakticheskaya9;

import java.util.ArrayList;

/**
 * Shows main menu of the program and allows a user to interact with the program by accepting input.
 * Can create, add to the list, delete from the list, show and compare different type of rooms.
 */
public class Prakticheskaya {
  static final int EXIT = 0;
  static final int CREATE_ROOM = 1;
  static final int DELETE_ROOM = 2;
  static final int DISPLAY_ROOMS = 3;
  static final int COMPARE_ROOMS = 4;

  /**
   * Allows a user to hold and manage in ArrayList different type of rooms
   * by accepting input and showing menu. Can create, print, compare, delete rooms from the list.
   * Runs an infinite while loop until user decides to exit.
   * @param args Arguments for command line.
   */
  public static void main(String[] args) {
    ArrayList<Room> roomsList = new ArrayList<Room>();
    String message = "\nEnter the number of a menu option: ";
    int userInput;

    Printer.printWelcome();
    do {
      Printer.printMenu();
      userInput = Validator.validateNumber(EXIT, COMPARE_ROOMS, message);
      switch (userInput) {
        case EXIT:
          System.out.println("\nExiting program");
          Validator.closeScanner();
          break;
        case CREATE_ROOM:
          RoomCreator.chooseAndCreateRoom(roomsList);
          break;
        case DELETE_ROOM:
          RoomManager.deleteRoom(roomsList);
          break;
        case DISPLAY_ROOMS:
          Printer.printRoomsList(roomsList);
          break;
        case COMPARE_ROOMS:
          RoomManager.compareRooms(roomsList);
          break;
        default:
          System.out.println("\nNo such option!");
      }
    } while (userInput != EXIT);
  } 
}