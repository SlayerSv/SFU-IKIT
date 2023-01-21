package prakticheskaya5;

/** Custom exception to throw when a user enteres not an integer number.*/
public class NotAnIntegerException extends NotANumberException {
  
  /**Default constructor that sets default error message if none specified. */
  public NotAnIntegerException() {
    super("Entered value must be an integer (whole number)");
  }

  /**Custom constructor for passing a custom error message. */
  public NotAnIntegerException(String errorMsg) {
    super(errorMsg);
  }
}
