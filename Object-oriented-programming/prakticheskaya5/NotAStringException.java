package prakticheskaya5;

import java.util.InputMismatchException;

/** Custom exception to throw when user enteres not a string.*/
public class NotAStringException extends InputMismatchException {
  
  /**Default constructor that sets default error message if none specified. */
  public NotAStringException() {
    super("Entered value must be a string!");
  }

  /**Custom constructor for passing a custom error message. */
  public NotAStringException(String errorMsg) {
    super(errorMsg);
  }
}
