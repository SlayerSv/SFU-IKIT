package PR1;

import java.util.ArrayList;

/**
 * Prints menus of the program and list of rooms to a user.
 */
public class Printer {

    static String mainMenu = """

        Choose what you want to do:
        1. Create a room
        2. Delete a room
        3. Show all rooms
        4. Compare 2 rooms

        0. Exit the program

        """;

    static String greeting = """

        Hello, welcome to the Room Manager program!
        In this program you can create, delete, compare different type of rooms
        """;

    static String createMenu = """

        Choose what type of a room you want to create:
        1. Room
        2. Class room
        3. Office room
        4. Gym room

        0. Go back

        """;

    /**
     * Prints welcome message to a user at the start of the program.
     */
    public static void printWelcome() {
        System.out.print(greeting);
    }

    /**
     * Prints the main menu of the program to a user.
     */
    public static void printMenu() {
        System.out.print(mainMenu);
    }

    /**
     * Prints create room menu of the program to a user.
     */
    public static void printCreateRoomMenu() {
        System.out.print(createMenu);
    }

    /**
     * Prints information about all rooms in an ArrayList to a user.
     * Uses toString() method to display information. Also displays hash code of
     * rooms.
     * 
     * @param roomsList ArrayList of rooms to be printed.
     */
    public static void printRoomsList(ArrayList<Room> roomsList) {
        if (roomsList.size() < 1) {
            System.out.print("\nNo rooms in the list!\n");
            return;
        }
        for (int i = 0; i < roomsList.size(); i++) {
            System.out.print("\nRoom number " + (i + 1) + ":\n" + roomsList.get(i).toString());
        }
    }
}