package prakticheskaya1;

/** 
 * Modifies matrices by sorting them.
 * Sorts matrices based on average values of its rows and columns.
 */
public class MatrixSorter {
  /** 
   * Creates a copy of a matrix and sorts this matrix rows based on average values of it`s rows.
   * Creates a copy of a matrix using copyMatrix function. Then
   * it compares rows by using isNextRowBigger function, if it returns "true",
   * then this function calls swapRows function to swap the rows.
   * This process continues, until no more rows needs swaping, which "modified" boolean variable
   * will indicate, after it will find no more rows that needs to be swapped.
   * @param matrix Initial matrix.
   * @return Copied matrix with sorted rows.
   */
  public int[][] sortMatrixRows(int[][] matrix) {
    boolean modified = false;
    int[][] newMatrix = copyMatrix(matrix);
    do {
      modified = false;
      for (int i = 0; i < newMatrix.length - 1; i++) {
        if (isNextRowBigger(newMatrix[i], newMatrix[i + 1])) {
          swapRows(newMatrix, i, i + 1);
          modified = true;
        }
      }
    } while (modified);
    return newMatrix;
  }

  /**
   * Checks if average value of the next row is bigger than average value of the current row.
   * Calculates the sums of all values of 2 rows of a matrix, then compares them to each other.
   * @param row1 current row of a matrix.
   * @param row2 row of a matrix that comes after the row1.
   * @return boolean that says if average value of row2 is bigger htan average value of row1.
   */
  private boolean isNextRowBigger(int[] row1, int[] row2) {
    boolean isBigger = false;
    int sumOfRow1 = 0;
    int sumOfRow2 = 0;
    for (int i = 0; i < row1.length; i++) {
      sumOfRow1 += row1[i];
    }
    for (int i = 0; i < row2.length; i++) {
      sumOfRow2 += row2[i];
    }
    if (sumOfRow1 < sumOfRow2) {
      isBigger = true;
    }
    return isBigger;
  }

  /**
   * Swaps 2 rows in a matrix.
   * @param matrix Matrix, in which rows needs to be swapped.
   * @param row1Index Index of the first row that needs to be swapped.
   * @param row2Index Index of the second row that needs to be swapped.
   */
  private void swapRows(int[][] matrix, int row1Index, int row2Index) {
    int[] row1 = matrix[row1Index];
    matrix[row1Index] = matrix[row2Index];
    matrix[row2Index] = row1;
    return;
  }

  /** Uses bubble sorting to sort matrix columns based on average values in it`s columns.
   * Creates a copy of the matrix, to not modify the initial matrix.
   * First, it compares columns by using isNextColumnBigger function, if it returns "true"
   * then this function calls swapColumns function to swap the columns.
   * This process continues, until no more columns needs swaping, which "modified" boolean variable
   * will indicate after it will find no columns that needs to be swapped.
   * @param matrix Initial matrix, in which columns need to be sorted.
   * @return Copy of the initial matrix with sorted columns.
   */
  public int[][] sortMatrixColumns(int[][] matrix) {
    boolean modified = false;
    int[][] newMatrix = copyMatrix(matrix);
    do {
      modified = false;
      for (int i = 0; i < newMatrix[0].length - 1; i++) {
        if (isNextColumnBigger(newMatrix, i, i + 1)) {
          swapColumns(newMatrix, i, i + 1);
          modified = true;
        }
      }
    } while (modified);
    return newMatrix;
  }

  /**
   * Checks if the average value of the next column is bigger than the average value of the current
   * column. Calculates the sums of all values of 2 columns of a matrix, then compares them to each
   * other.
   * @param matrix Matrix in which columns need to be compared.
   * @param col1 Current column of the matrix.
   * @param col2 Column after the current column of the matrix.
   * @return boolean value that says if the average value of the next column is bigger.
   */
  private boolean isNextColumnBigger(int[][] matrix, int col1, int col2) {
    boolean isBigger = false;
    int sumOfCol1 = 0;
    int sumOfCol2 = 0;
    for (int i = 0; i < matrix.length; i++) {
      sumOfCol1 += matrix[i][col1];
    }
    for (int i = 0; i < matrix.length; i++) {
      sumOfCol2 += matrix[i][col2];
    }
    if (sumOfCol1 < sumOfCol2) {
      isBigger = true;
    }
    return isBigger;
  }

  /**
   * Swaps 2 columns in a matrix.
   * Uses for loops to rewrite values in matrix columns, swapping them with each other.
   * @param matrix Initial matrix.
   * @param col1 Current column of the matrix.
   * @param col2 Column after the current column of the matrix.
   */
  private void swapColumns(int[][] matrix, int col1, int col2) {
    int[] column1 = new int[matrix.length];
    for (int i = 0; i < matrix.length; i++) {
      column1[i] = matrix[i][col1];
    }
    for (int i = 0; i < matrix.length; i++) {
      matrix[i][col1] = matrix[i][col2];
    }
    for (int i = 0; i < matrix.length; i++) {
      matrix[i][col2] = column1[i];
    }
  }

  /**
   * Creates a copy of a matrix.
   * @param matrix Initial matrix that needs to be copied.
   * @return Copied matrix.
   */
  private int[][] copyMatrix(int[][] matrix) {
    int[][] newMatrix = new int[matrix.length][matrix[0].length];
    for (int i = 0; i < newMatrix.length; i++) {
      for (int j = 0; j < newMatrix[0].length; j++) {
        newMatrix[i][j] = matrix[i][j];
      }
    }
    return newMatrix;
  }
}