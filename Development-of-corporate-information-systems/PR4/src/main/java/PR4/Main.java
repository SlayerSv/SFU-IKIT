package PR4;

/**
 * Shows main menu of the program and allows a user to interact with the program
 * by accepting input.
 * Can create, add, edit delete products from database, show and filter different
 * type of products.
 */
public class Main {
    static final int EXIT = 0;
    static final int PRINT_ALL = 1;
    static final int ADD_NEW = 2;
    static final int EDIT = 3;
    static final int DELETE = 4;
    static final int FILTER = 5;
    static final int RELOAD = 6;

    /**
     * Allows a user to manage product's database by accepting input and showing menu.
     * Can create, print, edit, delete, filter products.
     * Uses caching for optimization, data is stored in an ArrayList, can be reloaded
	 * on request.
	 * Detects possible synchronization problems with the database and reloads data automatically.
     * Runs an infinite while loop until user decides to exit.
     * 
     * @param args Arguments for command line.
     */
    public static void main(String[] args) {
    	ProductManager.loadData();
        String message = "Enter the number of a menu option: ";
        int userInput;

        Printer.printWelcome();
        do {
            Printer.printMenu();
            userInput = Validator.validateNumber(EXIT, RELOAD, message);
            switch (userInput) {
                case EXIT:
                    System.out.print("\nExiting program\n");
                    Validator.closeScanner();
                    ProductManager.context.close();
                    break;
                case PRINT_ALL:
                	ProductManager.printProducts();
                    break;
                case ADD_NEW:
                    ProductManager.addNewProduct();
                    break;
                case EDIT:
                    ProductManager.editProduct();
                    break;
                case DELETE:
                    ProductManager.deleteProduct();
                    break;
                case FILTER:
                    ProductManager.filterByPrice();
                    break;
                case RELOAD:
                	ProductManager.loadData();
                	break;
                default:
                    System.out.print("\nNo such option!\n");
            }
        } while (userInput != EXIT);
    }
}