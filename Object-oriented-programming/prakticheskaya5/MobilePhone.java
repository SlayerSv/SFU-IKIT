package prakticheskaya5;

import java.util.Scanner;

/**
 * Holds data and methods of a mobile phone object.
 * Has 2 constructors, default and with parameters. Has getters and setters
 * for all fields, runs validate functions to check entered data by a user and
 * catches custom exceptions if entered values are incorrect. Can calculate if a phone
 * is of a premium class. Can create new phones and print info.
 */
public class MobilePhone {
  static final double LOWEST_PHONE_PRICE = 0;
  static final double LOWEST_SCREEN_SIZE = 0;
  static final double HIGHEST_SCREEN_SIZE = 9;
  static final int LOWEST_MEMORY_SIZE = 0;
  static final double LOWEST_PRICE_OF_PREMIUM_PHONES = 300;
  static final double LOWEST_SCREEN_SIZE_OF_PREMIUM_PHONES = 6;
  static final int LOWEST_MEMORY_SIZE_OF_PREMIUM_PHONES = 256;

  private String name;
  private double price;
  private double screenSize;
  private int memory;
  private String color;
  private ValueValidator validator = new ValueValidator();
  Scanner scanner = validator.scanner;

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
   * Private custom constructor with parameters, used only by createNewPhone function
   * for creating a phone with known fields, that checks entered values.
   * Set to private to avoid entering incorrect data.
   * @param name Name of the phone.
   * @param price Price of the phone in USD.
   * @param screenSize Screen size of the phone in inches.
   * @param memory Memory size of the phone in GB.
   * @param color Color of the phone.
   */
  private MobilePhone(String name, double price, double screenSize, int memory, String color) {
    this.name = name;
    this.price = price;
    this.screenSize = screenSize;
    this.memory = memory;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  /**
   * Changes the name of the phone, checks that a user entered a string.
   * Throws and catches an exception if entered value is not a string, in that
   * case does nothing.
   */
  public void setName() {
    String message = "Enter a new name for the phone '" + this.name + "': ";
    String oldName = this.name;
    try {
      System.out.print(message);
      if (!scanner.hasNextDouble()) {
        this.name = scanner.nextLine();
        System.out.println("The new name for the phone '" + oldName + "' is now '"
            + this.name + "'!");
      } else {
        scanner.nextLine();
        throw new NotAStringException();
      }
    } catch (NotAStringException e) {
      // do nothing
    } finally {
      System.out.println("Getting back to menu...");
    }
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
    this.price = validator.validateNumber(LOWEST_PHONE_PRICE, message);
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
    this.screenSize = validator.validateNumber(LOWEST_SCREEN_SIZE, HIGHEST_SCREEN_SIZE, message);
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
    this.memory = validator.validateNumber(LOWEST_MEMORY_SIZE, message);
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
    this.color = validator.validateString(message);
    System.out.println("New color for the phone '" + this.name + "' is now '"
          + this.color + "'!");
  }

  /**
   * Decides if the phone is a premium class phone, based on it`s price,
   * screen size and memory size.
   * @return Boolean that says if a phone is a premium class phone.
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
    System.out.println("Name: " + name);
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
   * If entered data is correct, runs a private custom constructor
   * of a MobilePhone class and passes validated values. Only this function is allowed to call it.
   * @return Created phone by this method.
   */
  public MobilePhone createNewPhone() {
    String message;
    message = "Enter phone`s name: ";
    String name = validator.validateString(message);
    message = "Enter phone`s price: ";
    double price = validator.validateNumber(LOWEST_PHONE_PRICE, message);
    message = "Enter phone`s screen size: ";
    double screenSize = validator.validateNumber(LOWEST_SCREEN_SIZE,
                                                  HIGHEST_SCREEN_SIZE,
                                                  message);
    message = "Enter phone`s memory size: ";
    int memory = validator.validateNumber(LOWEST_MEMORY_SIZE, message);
    message = "Enter phone`s color: ";
    String color = validator.validateString(message);
    MobilePhone newPhone = new MobilePhone(name, price, screenSize, memory, color);
    System.out.println("New phone has been created!");
    newPhone.printInfo();
    return newPhone;
  }
}