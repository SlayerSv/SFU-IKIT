package prakticheskaya5;

import java.util.ArrayList;

/**
 * Allows a user to modify any stats of any phone in the list.
 */
public class PhoneModifier {
  ValueValidator validator = new ValueValidator();
  Printer printer = new Printer();
  
  /**
   * Prints a list of phones and asks a user to choose a phone for modifying.
   * Checks that the list of phones is not empty. If it is, throws an exception.
   * If it`s not, runs a function for modifying stats of the chosen phone.
   * Catches and handles custom exceptions if user entered incorrect data.
   * @param phones ArrayList of phones that can be modified.
   */
  public void modifyPhone(ArrayList<MobilePhone> phones) throws Throwable {
    try {
      if (phones.size() == 0) {
        throw new ListSizeException("The phones list is empty!");
      }
      int numberOfThePhone;
      System.out.println("\nChoose a phone you want to modify");
      printer.printPhones(phones);
      String message = "\nEnter a number of the phone you want to modify or type '0' to go back: ";
      numberOfThePhone = validator.validateNumber(0, phones.size(), message);
      if (numberOfThePhone == 0) {
        return;
      }
      System.out.println("You chose phone number " + numberOfThePhone);
      modifyPhoneStat(phones.get(numberOfThePhone - 1));
      modifyPhone(phones);
    } catch (ListSizeException e) {
      String errorMsg = "Failed to modify a phone";
      throw new ListOperationFailedException(errorMsg).initCause(e);
    }
  }

  /**
   * Allows a user to change any stat of the phone.
   * Accepts input from a user and runs appropriate setter method based on that.
   * @param phone Phone object that will be modified. 
   */
  private void modifyPhoneStat(MobilePhone phone) {
    System.out.println("");
    phone.printInfo();
    System.out.println("");
    System.out.println("Type 'back' if u want to go back");
    String message = "Enter phone`s field name you want to modify: ";
    String input = "";
    input = validator.validateString(message).toLowerCase();
    switch (input) {
      case "back":
        return;
      case "name":
        phone.setName();
        break;
      case "price":
        phone.setPrice();
        break;
      case "screen size":
        phone.setScreenSize();
        break;
      case "memory":
        phone.setMemory();
        break;
      case "color":
        phone.setColor();
        break;
      default:
        System.out.println("No such field!");
        break;
    }
    modifyPhoneStat(phone);
  }
}