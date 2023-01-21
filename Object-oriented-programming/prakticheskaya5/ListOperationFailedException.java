package prakticheskaya5;

/**
 * This exception is thrown when it is impossible to make certain operations
 * on a list.
 */
public class ListOperationFailedException extends Exception {

  /**Default constructor that sets standard error message if none is specified. */
  public ListOperationFailedException() {
    super("Something went wrong, failed to do this operation :(");
  }

  /**Custom constructor for passing a custom error message. */
  public ListOperationFailedException(String errorMsg) {
    super(errorMsg);
  }
}
