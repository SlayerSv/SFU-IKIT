package prakticheskaya3;

import java.util.ArrayList;

/**
 * Manages ArrayList of different classes of Rooms, can delete and compare rooms from the list.
 */
public class RoomManager {
  private static String message;

  /**
   * Deletes a room from ArrayList by index provided through input by a user.
   * Checks that the list is not empty and that user enters correct index of a room.
   * @param roomsList ArrayList of rooms from which room will be deleted.
   */
  public static void deleteRoom(ArrayList<Room> roomsList) {
    if (roomsList.size() < 1) {
      System.out.println("\nNo rooms in the list");
      return;
    }
    Printer.printRoomsList(roomsList);
    message = "\nChoose number of the room you want to delete ('0' to cancel): ";
    int input = Validator.validateNumber(0, roomsList.size(), message);
    if (input == 0) {
      return;
    }
    roomsList.remove(input - 1);
    System.out.println("\nThe room has been deleted!");
  }

  /**
   * Compares rooms in ArrayList by index  provided through input by a user.
   * Checks that the ArrayList has at least 2 rooms to compare and that user enters
   * correct indexes of rooms. Uses equals() function to compare rooms.
   * @param roomsList ArrayList of rooms that will be compared.
   */
  public static void compareRooms(ArrayList<Room> roomsList) {
    if (roomsList.size() < 2) {
      System.out.println("\nYou need at least 2 rooms in the list to compare!");
      return;
    }
    Printer.printRoomsList(roomsList);
    message = "\nEnter number of the first room to compare: ";
    int room1Index = Validator.validateNumber(1, roomsList.size(), message);
    message = "Enter number of the second room to compare: ";
    int room2Index = Validator.validateNumber(1, roomsList.size(), message);
    if (roomsList.get(room1Index - 1).equals(roomsList.get(room2Index - 1))) {
      System.out.println("\nThese 2 rooms are the same!");
    } else {
      System.out.println("\nThese 2 rooms are different!");
    }
  }
}