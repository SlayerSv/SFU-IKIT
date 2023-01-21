package prakticheskaya5;

/** Custom exception to throw when user enteres values which are not
 * in a certain range.
 */
public class IncorrectNumberRangeException extends Exception {
  
  /**Default constructor that sets default error message if none specified. */
  public IncorrectNumberRangeException() {
    super("Entered value must be in a certain range!");
  }

  /**Custom constructor for passing a custom error message. */
  public IncorrectNumberRangeException(String errorMsg) {
    super(errorMsg);
  }
}
