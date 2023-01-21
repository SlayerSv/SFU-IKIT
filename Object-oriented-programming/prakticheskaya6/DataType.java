package prakticheskaya6;

/**
 * Enum for holding data types that can be used by the MyQueue class.
 */
public enum DataType {
  STRING, INTEGER;

  public static DataType[] dataTypes = values();
  public static final int size = dataTypes.length;

  /**
   * Allows a user to change the data type that will be held in the queue. Iterates through
   * the array of enum values returned by {@code values()} and saved to {@code dataTypes} variable
   * and prints them on screen to the user. Accepts input from the user so he can choose the data
   * type he wants and returnes this value.
   * @return enum value that the user picked.
   */
  public static DataType changeDataType() {
    System.out.println("\nAvailable data types:\n");
    for (int i = 0; i < size; i++) {
      System.out.println((i + 1) + ". " + dataTypes[i]);
    }
    String message = "\nEnter the number of a data type: ";
    int dataTypeNumber = Validator.validateInt(message, 1, size);
    return dataTypes[dataTypeNumber - 1];
  }
}