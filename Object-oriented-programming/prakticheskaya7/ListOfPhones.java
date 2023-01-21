package prakticheskaya7;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class for holding and managing an {@code ArrayList} of {@code MobilePhone} objects.
 * Uses {@code Stream} API for most of its operations. 
 */
public class ListOfPhones {
  
  private ArrayList<MobilePhone> listOfPhones;

  /**
   * Checks if the {@code listOfPhones} variable is {@code null}, then runs {@code generatePhones()}
   * to initialize and add phones to the {@code listOfPhones}. If the list has been already
   * initialized notifies the user and does nothing. If {@code generatePhones()} returns null then
   * creates and empty {@code ArrayList}.
   */
  public void loadPhones() {
    if (listOfPhones == null) {
      listOfPhones = Optional.ofNullable(generatePhones()).orElse(new ArrayList<>());
      if (listOfPhones.size() != 0) {
        System.out.println("\nPhones has been successfully loaded!");
      } else {
        System.out.println("Empty list of phones has been created");
      }
    } else {
      System.out.println("\nList of phones has been already loaded!");
    }
  }

  /**
   * Gets a list of mobile phones, turns it into a {@code Stream} of
   * {@code MobilePhone} objects, iterates through that stream using {@code forEach()} and prints
   * information of all phones in the list.
   * 
   * <p>Calculates total and average prices of all phones by using {@code DoubleSummaryStatistics}.
   * Rounds those values to an accuracy of 0,01 and prints them to a user.
   * 
   * <p>Checks that the list is not {@code null} or empty.
   */
  public void printPhones() {
    if (listOfPhones == null) {
      System.out.println("\nYou must load a list of phones first!");
      return;
    }
    if (listOfPhones.size() == 0) {
      System.out.println("\nCan`t print the list of phones because it is empty!");
      return;
    }
    System.out.println("\nList of all available phones:");
    listOfPhones.stream().forEach(x -> x.printInfo());
    DoubleSummaryStatistics statistics = listOfPhones.stream()
                                                     .mapToDouble(MobilePhone::getPrice)
                                                     .summaryStatistics();
    double avgPrice = Math.round(statistics.getAverage() * 100) / 100.0;
    System.out.println("\nAverage price of the phones: $" + avgPrice);
    double totalPrice = Math.round(statistics.getSum() * 100) / 100.00;
    System.out.println("Total price of all phones: $" + totalPrice);
  }

  /**
   * Prints to a user a list of phones within a certain price range.
   * Accepts inputs from a user for a minimal and a maximal prices, then creates a {@code Stream} of
   * {@code MobilePhone} objects containing only phones within that price range by using
   * {@code filter()}. Prints those phones to a user and calculates their total price.
   */
  public void filterByPrice() {
    if (listOfPhones == null) {
      System.out.println("\nYou must load a list of phones first!");
      return;
    }
    if (listOfPhones.size() == 0) {
      System.out.println("\nCan`t filter the list because it is empty!");
      return;
    }
    String message = "\nEnter a minimal price of a phone: ";
    double minPrice = Validator.validateDouble(0, message);
    message = "Enter a maximal price of a phone: ";
    double maxPrice = Validator.validateDouble(minPrice, message);
    System.out.println("\nList of the phones with a price between " + minPrice + " and "
        + maxPrice + ":");
    listOfPhones.stream()
                .filter(x -> x.getPrice() > minPrice && x.getPrice() < maxPrice)
                .forEach(x -> x.printInfo());
    double totalPrice = listOfPhones.stream()
                                    .filter(x -> x.getPrice() > minPrice && x.getPrice() < maxPrice)
                                    .collect(Collectors.summingDouble(MobilePhone::getPrice));
    System.out.println("\nTotal price of phones: $" + totalPrice);
  }

  /**
   * Creates a {@code Stream} of unique {@code MobilePhone} objects, by removing all equal objects
   * with {@code distinct()}. Calculates and prints the total price of all phones.
   */
  public void printUniquePhones() {
    if (listOfPhones == null) {
      System.out.println("\nYou must load a list of phones first!");
      return;
    }
    if (listOfPhones.size() == 0) {
      System.out.println("\nCan`t print unique phones because the list of phones is empty!");
      return;
    }
    System.out.println("\nList of all unique phones:");
    listOfPhones.stream()
                .distinct()
                .forEach(x -> x.printInfo());
    double totalPrice = listOfPhones.stream()
                                    .distinct()
                                    .mapToDouble(MobilePhone::getPrice)
                                    .reduce(0.0, Double::sum);
    System.out.println("\nTotal price of all unique phones: $" + totalPrice);
  }

  /**
   * {@summary Prints all premium phones to a user.}
   * Runs {@code Collectors.partitioningBy()} to create a new {@code Map} with all phones
   * and corresponding boolean values that tells if the phone is premium. Then creates
   * a {@code List} of all premium phones.
   * After that creates a stream of premium phones and prints them on the screen with total price.
   */
  public void getPremiumPhones() {
    if (listOfPhones == null) {
      System.out.println("\nYou must load a list of phones first!");
      return;
    }
    if (listOfPhones.size() == 0) {
      System.out.println("\nCan`t print premium phones because the list of phones is empty!");
      return;
    }
    Map<Boolean, List<MobilePhone>> phones = listOfPhones.stream()
        .collect(Collectors.partitioningBy(x -> x.isPremium()));
    List<MobilePhone> premiumPhones = phones.get(true);
    System.out.println("\nList of all premium phones:");
    premiumPhones.stream().forEach(MobilePhone::printInfo);
    long count = premiumPhones.stream().count();
    System.out.println("\nTotal premium phones: " + count);
    double totalPrice = premiumPhones.stream()
                                     .mapToDouble(x -> x.getPrice())
                                     .reduce(0.0, (total, price) -> total + price);
    System.out.println("Total price of premium phone: $" + totalPrice);
  }

  /**
   * Runs {@code createNewPhone()} to create a new phone and adds it to the list of phones.
   */
  public void addNewPhone() {
    if (listOfPhones == null) {
      System.out.println("\nYou must load a list of phones first!");
      return;
    }
    MobilePhone newPhone = MobilePhone.createNewPhone();
    listOfPhones.add(newPhone);
    System.out.println("\nNew phone has been added to the list!");
  }

  /**
   * Initializes {@code listOfPhones} and creates and adds {@code MobilePhone} objects to it.
   * If passed arguments are not in a correct range throws an exception and returns {@code null}.
   * @throws IllegalArgumentException If passed arguments are not in a correct range.
   */
  private ArrayList<MobilePhone> generatePhones() {
    try {
      ArrayList<MobilePhone> generatedList;
      generatedList = new ArrayList<>();
      generatedList.add(new MobilePhone("iPhone", 1099, 6.1, 512, "black"));
      generatedList.add(new MobilePhone("iPhone", 1099, 6.1, 512, "black"));
      generatedList.add(new MobilePhone("Huawei", 699, 6.5, 256, "black"));
      generatedList.add(new MobilePhone("Samsung", 899, 6.7, 512, "red"));
      generatedList.add(new MobilePhone("Xiaomi", 599, 5.7, 128, "pink"));
      generatedList.add(new MobilePhone("Xiaomi", 599, 5.7, 128, "pink"));
      generatedList.add(new MobilePhone("Vivo", 799, 6.3, 256, "white"));
      generatedList.add(new MobilePhone("Moto", 499, 5.2, 128, "yellow"));
      generatedList.add(new MobilePhone("LG", 999, 6.4, 512, "silver"));
      generatedList.add(new MobilePhone("Realme", 399, 5.1, 64, "blue"));
      generatedList.add(new MobilePhone("OnePlus", 299, 4.7, 32, "green"));
      return generatedList;
    } catch (IllegalArgumentException e) {
      System.out.println(e);
      System.out.println("Failed to load phones");
      return null;
    }
  }
}