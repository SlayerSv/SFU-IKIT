package prakticheskaya2;

import java.util.ArrayList;

/**
 * Allows a user to modify any stats of any phone in the list.
 */
public class PhoneModifier {
  ValueValidator validator = new ValueValidator();
  Printer printer = new Printer();
  
  /**
   * Prints a list of phones and asks a user to choose a phone for modifying.
   * After that runds a function for modifying stats in the choosen phone.
   * @param phones ArrayList of phones that can be modified.
   */
  public void modifyPhone(ArrayList<MobilePhone> phones) {
    if (phones.size() == 0) {
      System.out.println("The phone list is empty!");
      return;
    }
    printer.printPhones(phones);
    System.out.println("");
    System.out.print("Enter a number of the phone you want to modify or type '0' to go back: ");
    int  numberOfThePhone = validator.validateNumber(0, phones.size());
    if (numberOfThePhone == 0) {
      return;
    }
    System.out.println("You chose phone number " + numberOfThePhone);
    modifyPhoneStat(phones.get(numberOfThePhone - 1));
    modifyPhone(phones);
  }

  /**
   * Allows a user to change any stat of the phone.
   * Accepts input from a user and runs appropriate setter method based on that.
   * @param phone Phone object that will be modified. 
   */
  private void modifyPhoneStat(MobilePhone phone) {
    phone.printInfo();
    System.out.println("");
    System.out.println("Type 'back' if u want to go back");
    System.out.print("Enter phone`s field name you want to modify: ");
    String input = validator.validateString().toLowerCase();
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