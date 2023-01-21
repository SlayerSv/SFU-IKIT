package prakticheskaya7;

//import java.lang.IllegalArgumentException;
/**
 * Holds data and methods of a mobile phone objects.
 * Has 2 constructors, default and with parameters. Has getters and setters
 * for all fields, runs validate functions to check entered data by a user and
 * throws exceptions if entered values are incorrect. Can calculate if a phone
 * is of a premium class. Can create new phones and print info.
 */
public class MobilePhone {
  static final double LOWEST_PHONE_PRICE = 0;
  static final double LOWEST_SCREEN_SIZE = 1;
  static final double HIGHEST_SCREEN_SIZE = 8;
  static final int LOWEST_MEMORY_SIZE = 1;
  static final double LOWEST_PRICE_OF_PREMIUM_PHONES = 500;
  static final double LOWEST_SCREEN_SIZE_OF_PREMIUM_PHONES = 6;
  static final int LOWEST_MEMORY_SIZE_OF_PREMIUM_PHONES = 256;

  private String name;
  private double price;
  private double screenSize;
  private int memory;
  private String color;

  /**
   * Default constructor, used for creating a phone with unknown fields.
   */
  public MobilePhone() {
    screenSize = 0;
    price = 0;
    name = "Unknown name";
    memory = 0;
    color = "Unknown color";
  }

  /**
   * Custom constructor with parameters, can be used to create a new phone with specified
   * parameters. Checks that entered values are in correct range, otherwise throws exception.
   * @param name Name of the phone.
   * @param price Price of the phone in USD.
   * @param screenSize Screen size of the phone in inches.
   * @param memory Memory size of the phone in GB.
   * @param color Color of the phone.
   * @throws IllegalArgumentException If passed arguments are not in a correct range.
   */
  public MobilePhone(String name, double price, double screenSize, int memory, String color) {
    this.name = name;
    if (price < 0) {
      throw new IllegalArgumentException("Price of the phone " + this.name
          + " must be greater or equal to " + LOWEST_PHONE_PRICE + "!");
    }
    this.price = Math.round(price * 100) / 100.00;
    if (screenSize < LOWEST_SCREEN_SIZE || screenSize > HIGHEST_SCREEN_SIZE) {
      throw new IllegalArgumentException("Screen size of the phone " + this.name
          + " must be between " + LOWEST_SCREEN_SIZE + " and " + HIGHEST_SCREEN_SIZE + "!");
    }
    this.screenSize = Math.round(screenSize * 10) / 10.0;
    if (memory < 0) {
      throw new IllegalArgumentException("Memory of the phone " + this.name
          + " must be greater or equal to " + LOWEST_MEMORY_SIZE + "!");
    }
    this.memory = memory;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  /**Changes the name of the phone.*/
  public void setName() {
    String message = "Enter a new name for the phone '" + this.name + "': ";
    String oldName = this.name;
    System.out.print(message);
    System.out.println("The new name for the phone '" + oldName + "' is now '" + this.name + "'!");
  }
  

  public double getPrice() {
    return price;
  }

  /**
   * Changes the price of the phone, runs validate function to check entered data
   * to make sure it`s a number in a certain range.
   */
  public void setPrice() {
    String message = "Enter a new price for the phone '" + this.name
        + "' (current price is $" + this.price + "): ";
    double newPrice = Validator.validateDouble(LOWEST_PHONE_PRICE, message);
    this.price = Math.round(newPrice * 100) / 100.00;
    System.out.println("New price for the phone '" + this.name + "' is now $"
        + this.price + "!");
  }

  public double getScreenSize() {
    return screenSize;
  }

  /**
   * Changes the screen size of the phone, runs validate function to check entered data
   * to make sure it`s a number in a certain range.
   */
  public void setScreenSize() {
    String message = "Enter a new screen size for the phone '" + this.name
        + "' (current screen size is " + this.screenSize + "): ";
    double newScreenSize = Validator.validateDouble(LOWEST_SCREEN_SIZE,
                                                    HIGHEST_SCREEN_SIZE,
                                                    message);
    this.screenSize = Math.round(newScreenSize * 10) / 10.0;
    System.out.println("New screensize for the phone '" + this.name + "' is now "
        + this.screenSize + "!");
  }

  public int getMemory() {
    return memory;
  }

  /**
   * Changes the memory size of the phone, runs validate function to check entered data
   * to make sure it`s a number in a certain range.
   */
  public void setMemory() {
    String message = "Enter new memory size for the phone '" + this.name
          + "' (current memory size is " + this.memory + "GB): ";
    this.memory = Validator.validateInt(LOWEST_MEMORY_SIZE, message);
    System.out.println("Memory size of the phone '" + name + "' is now " + this.memory + " GB!");
  }

  public String getColor() {
    return color;
  }

  /**
   * Changes the color of the phone, runs validate function to check entered data
   * to make sure it`s a string.
   */
  public void setColor() {
    String message = "Enter a new color for the phone '" + this.name
        + "' (current color is " + this.color + "): ";
    this.color = Validator.validateString(message);
    System.out.println("New color for the phone '" + this.name + "' is now '"
          + this.color + "'!");
  }

  /**
   * Decides if the phone is a premium class phone, based on it`s price, screen size
   * and memory size.
   * @return {@code true} if a phone has all fields greater or equal to certain values set for
   *      premium phones, {@code null} otherwise.
   */
  public boolean isPremium() {
    boolean isPremium = false;
    if (screenSize >= LOWEST_SCREEN_SIZE_OF_PREMIUM_PHONES
        && price >= LOWEST_MEMORY_SIZE_OF_PREMIUM_PHONES
        && memory >= LOWEST_MEMORY_SIZE_OF_PREMIUM_PHONES) {
      isPremium = true;
    }
    return isPremium;
  }

  /**
   * Prints phone`s stats and says if a phone is a premium class phone.
   */
  public void printInfo() {
    System.out.println("\nName: " + name);
    System.out.println("Price: $" + price);
    System.out.println("Screen size: " + screenSize + " inches");
    System.out.println("Memory: " + memory + " GB");
    System.out.println("Color: " + color);
    System.out.print("Premuim phone: ");
    if (isPremium()) {
      System.out.println("Yes");
    } else {
      System.out.println("No");
    }
  }

  /**
   * Method for creating a phone with fields values entered by a user. Runs validate
   * functions to check that user entered correct values.
   * If entered data is correct, runs a custom constructor of a MobilePhone class
   * and passes validated values.
   * @return Created phone by this method.
   */
  public static MobilePhone createNewPhone() {
    String message;
    message = "Enter phone`s name: ";
    String name = Validator.validateString(message);
    message = "Enter phone`s price: ";
    double price = Validator.validateDouble(LOWEST_PHONE_PRICE, message);
    message = "Enter phone`s screen size: ";
    double screenSize = Validator.validateDouble(LOWEST_SCREEN_SIZE,
                                                  HIGHEST_SCREEN_SIZE,
                                                  message);
    message = "Enter phone`s memory size: ";
    int memory = Validator.validateInt(LOWEST_MEMORY_SIZE, message);
    message = "Enter phone`s color: ";
    String color = Validator.validateString(message);
    MobilePhone newPhone = new MobilePhone(name, price, screenSize, memory, color);
    System.out.println("New phone has been created!");
    newPhone.printInfo();
    return newPhone;
  }

  @Override
  public boolean equals(Object object) {
    boolean isEqual;
    if (this == object) {
      isEqual = true;
    } else if (object == null || this.getClass() != object.getClass()) {
      isEqual = false;
    } else {
      MobilePhone comparedPhone = (MobilePhone) object;
      if (this.getName().toLowerCase().equals(comparedPhone.getName().toLowerCase())
          && this.getPrice() == comparedPhone.getPrice()
          && this.getScreenSize() == comparedPhone.getScreenSize()
          && this.getMemory() == comparedPhone.getMemory()
          && this.getColor().toLowerCase().equals(comparedPhone.getColor().toLowerCase())
          ) {
        isEqual = true;
      } else {
        isEqual = false;
      }
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    Double price = this.price;
    Double screenSize = this.screenSize;
    return this.name.toLowerCase().hashCode() + price.hashCode() + screenSize.hashCode()
        + this.memory + this.color.toLowerCase().hashCode();
  }
}