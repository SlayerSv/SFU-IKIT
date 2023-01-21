package prakticheskaya5;

/**This exception is thrown when a size of a list is not in a needed range. */
public class ListSizeException extends Exception {
  
  /**Default constructor that sets standard error message if none is specified. */
  public ListSizeException() {
    super("The size of the list is not enough for this operation");
  }

  /**Custom constructor for passing a custom error message. */
  public ListSizeException(String errorMsg) {
    super(errorMsg);
  }
}
