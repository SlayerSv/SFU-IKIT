package prakticheskaya1;

import java.util.ArrayList;

/**
 * Manages ArrayList of different classes of Rooms, can delete and compare rooms
 * from the list.
 */
public class RoomManager {
    private static String message;

    /**
     * Deletes a room from ArrayList by index provided through input by a user.
     * Checks that the list is not empty and that user enters correct index of a
     * room.
     * 
     * @param roomsList ArrayList of rooms from which room will be deleted.
     */
    public static void deleteRoom(ArrayList<Room> roomsList) {
        if (roomsList.size() < 1) {
            System.out.print("\nNo rooms in the list\n");
            return;
        }
        Printer.printRoomsList(roomsList);
        message = "\nChoose number of the room you want to delete ('0' to cancel): ";
        int input = Validator.validateNumber(0, roomsList.size(), message);
        if (input == 0) {
            return;
        }
        roomsList.remove(input - 1);
        System.out.print("\nThe room has been deleted!\n");
    }

    /**
     * Compares rooms in ArrayList by index provided through input by a user.
     * Checks that the ArrayList has at least 2 rooms to compare and that user
     * enters
     * correct indexes of rooms. Uses equals() function to compare rooms.
     * 
     * @param roomsList ArrayList of rooms that will be compared.
     */
    public static void compareRooms(ArrayList<Room> roomsList) {
        if (roomsList.size() < 2) {
            System.out.print("\nYou need at least 2 rooms in the list to compare!\n");
            return;
        }
        Printer.printRoomsList(roomsList);
        message = "\nEnter number of the first room to compare: ";
        int room1Index = Validator.validateNumber(1, roomsList.size(), message);
        message = "Enter number of the second room to compare: ";
        int room2Index = Validator.validateNumber(1, roomsList.size(), message);
        if (roomsList.get(room1Index - 1).equals(roomsList.get(room2Index - 1))) {
            System.out.print("\nThese 2 rooms are the same!\n");
        } else {
            System.out.print("\nThese 2 rooms are different!\n");
        }
    }
}