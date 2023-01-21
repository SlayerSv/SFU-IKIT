package prakticheskaya2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Sorts ArrayList of the mobile phones based on different fields.
 * Can sort based on the name, price, screen size, memory size and color of the phones.
 */
public class PhoneSorter {
  static final int CANCEL = 0;
  static final int SORT_BY_NAME = 1;
  static final int SORT_BY_PRICE = 2;
  static final int SORT_BY_SCREEN_SIZE = 3;
  static final int SORT_BY_MEMORY = 4;
  static final int SORT_BY_COLOR = 5;

  ValueValidator validator = new ValueValidator();
  Printer printer = new Printer();

  /**
   * Shows and allows a user to choose different sorting options.
   * Accepts input from a user and runs appropriate sorting method.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  public void sortPhones(ArrayList<MobilePhone> phones) {
    if (phones.size() == 0) {
      System.out.println("The phone list is empty!");
      return;
    }
    printer.printPhoneFields();
    System.out.print("Enter number of the field by which you want to sort phones: ");
    int input = validator.validateNumber(CANCEL, SORT_BY_COLOR);
    switch (input) {
      case CANCEL:
        return;
      case SORT_BY_NAME:
        sortPhonesByName(phones);
        break;
      case SORT_BY_PRICE:
        sortPhonesByPrice(phones);
        break;
      case SORT_BY_SCREEN_SIZE:
        sortPhonesByScreenSize(phones);
        break;
      case SORT_BY_MEMORY:
        sortPhonesByMemory(phones);
        break;
      case SORT_BY_COLOR:
        sortPhonesByColor(phones);
        break;
      default:
        System.out.println("No such option! Try again");
        sortPhones(phones);
    }
  }

  /**
   * Sorts list of the mobile phones by their names.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  private void sortPhonesByName(ArrayList<MobilePhone> phones) {
    boolean modified = false;
    do {
      modified = false;
      for (int i = 0; i < phones.size() - 1; i++) {
        if (phones.get(i).getName().toLowerCase()
            .compareTo(phones.get(i + 1).getName().toLowerCase()) > 0) {
          Collections.swap(phones, i, i + 1);
          modified = true;
        }
      }
    } while (modified);
    System.out.println("Phones have been sorted! New list:");
    printer.printPhones(phones);
  }

  /**
   * Sorts list of the mobile phones by their color.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  public void sortPhonesByColor(ArrayList<MobilePhone> phones) {
    boolean modified = false;
    do {
      modified = false;
      for (int i = 0; i < phones.size() - 1; i++) {
        if (phones.get(i).getColor().toLowerCase()
            .compareTo(phones.get(i + 1).getColor().toLowerCase()) > 0) {
          Collections.swap(phones, i, (i + 1));
          modified = true;
        }
      }
    } while (modified);
    System.out.println("Phones have been sorted! New list:");
    printer.printPhones(phones);
  }

  /**
   * Sorts list of the mobile phones by their screen size.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  public void sortPhonesByScreenSize(ArrayList<MobilePhone> phones) {
    boolean modified = false;
    do {
      modified = false;
      for (int i = 0; i < phones.size() - 1; i++) {
        if (phones.get(i).getScreenSize() > phones.get(i + 1).getScreenSize()) {
          Collections.swap(phones, i, (i + 1));
          modified = true;
        }
      }
    } while (modified);
    System.out.println("Phones have been sorted! New list::");
    printer.printPhones(phones);
  }

  /**
   * Sorts list of the mobile phones by their memory size.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  public void sortPhonesByMemory(ArrayList<MobilePhone> phones) {
    boolean modified = false;
    do {
      modified = false;
      for (int i = 0; i < phones.size() - 1; i++) {
        if (phones.get(i).getMemory() > phones.get(i + 1).getMemory()) {
          Collections.swap(phones, i, (i + 1));
          modified = true;
        }
      }
    } while (modified);
    System.out.println("Phones have been sorted! New list:");
    printer.printPhones(phones);
  }

  /**
   * Sorts list of the mobile phones by their price.
   * @param phones ArrayList of the mobile phones that will be sorted.
   */
  public void sortPhonesByPrice(ArrayList<MobilePhone> phones) {
    boolean modified = false;
    do {
      modified = false;
      for (int i = 0; i < phones.size() - 1; i++) {
        if (phones.get(i).getPrice() > phones.get(i + 1).getPrice()) {
          Collections.swap(phones, i, (i + 1));
          modified = true;
        }
      }
    } while (modified);
    System.out.println("Phones have been sorted! New list:");
    printer.printPhones(phones);
  }
}