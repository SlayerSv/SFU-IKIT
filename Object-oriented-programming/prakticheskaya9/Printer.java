package prakticheskaya9;

import java.util.ArrayList;

/**
 * Prints menus of the program and list of rooms to a user.
 */
public class Printer {

  /**
   * Prints welcome message to a user at the start of the program.
   */
  public static void printWelcome() {
    System.out.println("Hello, welcome to the Room Manager program!");
    System.out.println("In this program you can create, delete, compare different type of rooms");
  }

  /**
   * Prints the main menu of the program to a user.
   */
  public static void printMenu() {
    System.out.println("");
    System.out.println("Choose what you want to do:");
    System.out.println("1. Create a room");
    System.out.println("2. Delete a room");
    System.out.println("3. Show all rooms");
    System.out.println("4. Compare 2 rooms");
    System.out.println("");
    System.out.println("0. Exit the program");
  }

  /**
   * Prints create room menu of the program to a user.
   */
  public static void printCreateRoomMenu() {
    System.out.println("");
    System.out.println("Choose what type of a room you want to create:");
    System.out.println("1. Create a room");
    System.out.println("2. Create a classroom");
    System.out.println("3. Create an office room");
    System.out.println("4. Create a gym room");
    System.out.println("");
    System.out.println("0. Go back");
  }

  /**
   * Prints information about all rooms in an ArrayList to a user.
   * Uses toString() method to display information. Also displays hash code of rooms.
   * @param roomsList ArrayList of rooms to be printed.
   */
  public static void printRoomsList(ArrayList<Room> roomsList) {
    if (roomsList.size() < 1) {
      System.out.println("\nNo rooms in the list!");
      return;
    }
    for (int i = 0; i < roomsList.size(); i++) {
      System.out.println("");
      System.out.println("Room number " + (i + 1) + ":");
      System.out.println(roomsList.get(i).toString());
      System.out.println("Hash code: " + roomsList.get(i).hashCode());
    }
  }
}