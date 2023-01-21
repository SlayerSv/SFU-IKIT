package prakticheskaya2;

/**
 * Holds data and methods of a mobile phone.
 * Has 2 constructors, default and with paramaters. Has getters and setters
 * for all fields, validates entered data by a user. Can calculate if a phone
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
   * Custom constructor with parameters, used for creating a phone with known fields.
   * @param name Name of the phone.
   * @param price Price of the phone in USD.
   * @param screenSize Screen size of the phone in inches.
   * @param memory Memory size of the phone in GB.
   * @param color Color of the phone.
   */
  public MobilePhone(String name, double price, double screenSize, int memory, String color) {
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
   * Changes the name of the phone, validates entered data to make sure it`s a string.
   */
  public void setName() {
    System.out.print("Enter a new name for the phone '" + this.name + "': ");
    String oldName = this.name;
    this.name = validator.validateString();
    System.out.println("The new name for the phone '" + oldName + "' is now '" + this.name + "'!");
  }

  public double getPrice() {
    return price;
  }

  /**
   * Changes the price of the phone, validates entered data to make sure it`s a number
   * in appropriate range.
   */
  public void setPrice() {
    System.out.print("Enter a new price for the phone '" + this.name
        + "' (current price is $" + this.price + "): ");
    this.price = validator.validateNumber(LOWEST_PHONE_PRICE);
    System.out.println("New price for the phone '" + this.name + "' is now $"
        + this.price + "!");
  }

  public double getScreenSize() {
    return screenSize;
  }

  /**
   * Changes the screen size of the phone, validates entered data to make sure it`s a number
   * in appropriate range.
   */
  public void setScreenSize() {
    System.out.print("Enter a new screen size for the phone '" + this.name
        + "' (current screen size is " + this.screenSize + "): ");
    this.screenSize = validator.validateNumber(LOWEST_SCREEN_SIZE, HIGHEST_SCREEN_SIZE);
    System.out.println("New screensize for the phone '" + this.name + "' is now "
        + this.screenSize + "!");
  }

  public int getMemory() {
    return memory;
  }

  /**
   * Changes the memory size of the phone, validates entered data to make sure it`s a number
   * in appropriate range.
   */
  public void setMemory() {
    System.out.print("Enter new memory size for the phone '" + this.name
          + "' (current memory size is " + this.memory + "GB): ");
    this.memory = validator.validateNumber(LOWEST_MEMORY_SIZE);
    System.out.println("Memory size of the phone '" + name + "' is now " + this.memory + " GB!");
  }

  public String getColor() {
    return color;
  }

  /**
   * Changes the color of the phone, validates entered data to make sure it`s a string.
   */
  public void setColor() {
    System.out.print("Enter a new color for the phone '" + this.name
        + "' (current color is " + this.color + "): ");
    this.color = validator.validateString();
    System.out.println("New color for the phone '" + this.name + "' is now '" + this.color + "'!");
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
   * Create a new phone by accepting and validating data from a user.
   * @return Created phone by this method.
   */
  public MobilePhone createNewPhone() {
    System.out.print("Enter phone`s name: ");
    String name = validator.validateString();
    System.out.print("Enter phone`s price: ");
    double price = validator.validateNumber(LOWEST_PHONE_PRICE);
    System.out.print("Enter phone`s screen size: ");
    double screenSize = validator.validateNumber(LOWEST_SCREEN_SIZE, HIGHEST_SCREEN_SIZE);
    System.out.print("Enter phone`s memory size: ");
    int memory = validator.validateNumber(LOWEST_MEMORY_SIZE);
    System.out.print("Enter phone`s color: ");
    String color = validator.validateString();
    MobilePhone newPhone = new MobilePhone(name, price, screenSize, memory, color);
    System.out.println("New phone has been created!");
    newPhone.printInfo();
    return newPhone;
  }
}