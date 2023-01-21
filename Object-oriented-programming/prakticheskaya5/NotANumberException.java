package prakticheskaya5;

import java.util.InputMismatchException;

/** Custom exception to throw when user enteres not a number.*/
public class NotANumberException extends InputMismatchException {
  
  /**Default constructor that sets default error message if none specified. */
  public NotANumberException() {
    super("Entered value must be a number!");
  }

  /**Custom constructor for passing a custom error message. */
  public NotANumberException(String errorMsg) {
    super(errorMsg);
  }
}
