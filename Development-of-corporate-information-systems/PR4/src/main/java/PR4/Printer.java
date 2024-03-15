package PR4;

/**
 * Prints different messages of the program
 */
public class Printer {

    static final String mainMenu = """

        Choose what you want to do:
        1. Print all products
        2. Add a new product 
        3. Edit product
        4. Delete product
        5. Filter products by price
        
        6. Reload data

        0. Exit the program

        """;

    static final String greeting = """

        Hello, welcome to the Product Manager program!
        In this program you can create, delete, edit, filter products
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
}