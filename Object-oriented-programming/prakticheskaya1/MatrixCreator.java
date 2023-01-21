package prakticheskaya1;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 *  This class is responsible for creating matrices.
 *  It can create matrices manually, by accepting input from user,
 *  or it can generate matrices automatically with random values.
 */
public class MatrixCreator {
  static final int MIN_NUMBER_OF_ROWS = 3;
  static final int MAX_NUMBER_OF_ROWS = 6;
  static final int MIN_NUMBER_OF_COLUMNS = 3;
  static final int MAX_NUMBER_OF_COLUMNS = 6;
  static final int MIN_MATRIX_VALUE = -99;
  static final int MAX_MATRIX_VALUE = 99;

  Printer printer = new Printer();
  private Scanner scanner = new Scanner(System.in);
  private int numberOfRows = 0;
  private int numberOfColumns = 0;
  private int[][] matrix;

  /** 
   * Creates a matrix manually by requesting all needed inputs from the user.
   * Requests numbers of rows, columns, then values for all fields. Validates
   * entered values by using validateInput function.
   * @return created matrix by the user.
   */
  public int[][] createMatrix() {
    System.out.println("Enter number of rows: ");
    numberOfRows = validateInput(MIN_NUMBER_OF_ROWS, MAX_NUMBER_OF_ROWS);
    System.out.println("Enter number of columns: ");
    numberOfColumns = validateInput(MIN_NUMBER_OF_COLUMNS, MAX_NUMBER_OF_COLUMNS);
    matrix = new int[numberOfRows][numberOfColumns];
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        System.out.print("Enter matrix value for row " + (i + 1) + " column " + (j + 1) + ": ");
        matrix[i][j] = validateInput(MIN_MATRIX_VALUE, MAX_MATRIX_VALUE);
        System.out.println("Current matrix:");
        printer.printMatrix(matrix);
      }
    }
    System.out.println("Matrix created!");
    return matrix;
  }

  /**
   * Checks that a user enters correct values.
   * Runs an infinite loop, until user enters correct value. Value
   * must be a number in a certain range.
   * @param minValue Lowest value that can be accepted as an input.
   * @param maxValue Highest value that can be accepted as an input.
   * @return value, entered by a user, that was sucessfully validated by this method.
   */
  public int validateInput(int minValue, int maxValue) {
    int input = minValue - 1;
    while (input < minValue || input > maxValue) {
      try {
        input = scanner.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Input must be a number!");
      } finally {
        scanner.nextLine();
      }
      if (input < minValue || input > maxValue) {
        System.out.println("Number must be between " + minValue + " and " + maxValue);
      }
    }
    return input;
  }

  /**
   * Automatically generates random matrix with values in certain ranges.
   * Uses constants declared in it`s class for determing ranges for number of rows,
   * columns and matrix values. Creates a matrix with all values generated randomly
   * in those ranges. Displays the resulting matrix.
   * @return Generated random matrix.
   */
  public int[][] generateRandomMatrix() {
    Random random = new Random();
    numberOfRows = random.nextInt(MAX_NUMBER_OF_ROWS + 1 - MIN_NUMBER_OF_ROWS) + MIN_NUMBER_OF_ROWS;
    numberOfColumns = random.nextInt(MAX_NUMBER_OF_COLUMNS + 1 - MIN_NUMBER_OF_COLUMNS)
        + MIN_NUMBER_OF_COLUMNS;
    int[][] matrix = new int[numberOfRows][numberOfColumns];
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        matrix[i][j] = random.nextInt(MAX_MATRIX_VALUE + 1 - MIN_MATRIX_VALUE) + MIN_MATRIX_VALUE;
      }
    }
    System.out.println("Random matrix generated! Here it is:");
    printer.printMatrix(matrix);
    return matrix;
  }

  public void closeScanner() {
    scanner.close();
  }
}