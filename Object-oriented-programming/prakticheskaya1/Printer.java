package prakticheskaya1;

/**
 * Class for printing various information for a user.
 * It prints greetings, main menu of the program and matrices.
 */
public class Printer {
  /**
   * Prints main menu of the program.
   */
  public void printMenu() {
    System.out.println("Choose an option:");
    System.out.println("1. Enter data for matrix manually");
    System.out.println("2. Generate random matrix automatically");
    System.out.println("3. Sort matrix by rows and columns");
    System.out.println("4. Display result");
    System.out.println("0. Exit the program");
  }
  
  /**
   * Prints greeting of a user when the porgram starts.
   */
  public void printWelcome() {
    System.out.println("Welcome to Matrix sorting program!");
    System.out.println("This program can sort matrices based on");
    System.out.println("average values in rows and columns. \n");
  }

  /**
   * Prints a matrix on the screen, with average values of its rows and columns.
   * @param matrix Matrix that needs to be printed.
   */
  public void printMatrix(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      int rowSum = 0;
      for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + "\t");
        rowSum += matrix[i][j];
      }
      System.out.print("(~" + rowSum / matrix[i].length + ")");
      System.out.println();
    }
    int[] columnAvg = new int[matrix[0].length];
    for (int i = 0; i < matrix[0].length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        columnAvg[i] += matrix[j][i];
      }
    }
    for (int i = 0; i < columnAvg.length; i++) {
      System.out.print("(~" + columnAvg[i] / matrix.length + ")\t");
    }
    System.out.println("\n");
  }
}