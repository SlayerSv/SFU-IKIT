package prakticheskaya1;

/**
 * Displays main menu of the program, accepts input from a user and acts based on it.
 * It shows the user the main menu of the program with all avalable options.
 * By taking user`s input, program makes appropriate actions.
 * Most options are for creating and modifying matrices.
 */
public class Practicheskaya1 {
  static final int EXIT_PROGRAM = 0;
  static final int CREATE_MATRIX = 1;
  static final int GENERATE_RANDOM_MATRIX = 2;
  static final int SORT_MATRIX = 3;
  static final int DISPLAY_RESULT = 4;

  /**
   * Shows main menu and allows a user to make certain actions by accepting input.
   * Loops through the menu, takes input from the user allowing user to interact with the program.
   * Mostly creates and modifies matrices based on user actions.
   * By entering "0" program terminates, otherwise loops indefinitely.
   */
  public static void main(String[] args) {
    int userInput;
    MatrixCreator matrixCreator = new MatrixCreator();
    MatrixSorter matrixSorter = new MatrixSorter();
    Printer printer = new Printer();
    int[][] matrix = new int[0][0];
    int[][] matrixSortedByRows = new int[0][0];
    int[][] matrixSortedByRowsAndColumns = new int[0][0];
    boolean isMatrixCreated = false;
    boolean isMatrixSorted = false;
    printer.printWelcome();
    do {
      printer.printMenu();
      userInput = matrixCreator.validateInput(EXIT_PROGRAM, DISPLAY_RESULT);
      switch (userInput) {
        case EXIT_PROGRAM:
          System.out.println("Exiting the program");
          matrixCreator.closeScanner();
          break;
        case CREATE_MATRIX:
          matrix = matrixCreator.createMatrix();
          isMatrixCreated = true;
          isMatrixSorted = false;
          break;
        case GENERATE_RANDOM_MATRIX:
          matrix = matrixCreator.generateRandomMatrix();
          isMatrixCreated = true;
          isMatrixSorted = false;
          break;
        case SORT_MATRIX:
          if (isMatrixCreated && !isMatrixSorted) {
            matrixSortedByRows = matrixSorter.sortMatrixRows(matrix);
            matrixSortedByRowsAndColumns = matrixSorter.sortMatrixColumns(matrixSortedByRows);
            isMatrixSorted = true;
            System.out.println("The matrix has been sorted!\n");
          } else if (isMatrixCreated && isMatrixSorted) {
            System.out.println("The matrix is already sorted!\n");
          } else {
            System.out.println("You need to create a matrix first!\n");
          }
          break;
        case DISPLAY_RESULT:
          if (isMatrixCreated && isMatrixSorted) {
            System.out.println("Your initial matrix:");
            printer.printMatrix(matrix);
            System.out.println("Matrix sorted by rows:");
            printer.printMatrix(matrixSortedByRows);
            System.out.println("Matrix sorted by rows and columns");
            printer.printMatrix(matrixSortedByRowsAndColumns);
          } else if (isMatrixCreated && !isMatrixSorted) {
            System.out.println("You need to sort matrix first!\n");
          } else {
            System.out.println("You need to create and sort matrix first!\n");
          }
          break;
        default:
          System.out.println("No such option, try again");
      }
    } while (userInput != EXIT_PROGRAM);
  }
}