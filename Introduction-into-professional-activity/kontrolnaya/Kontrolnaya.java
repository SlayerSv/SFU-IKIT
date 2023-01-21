package kontrolnaya;

public class Kontrolnaya {
  static final int EXIT = 0;
  static final int E_TO_POWER_OF_X = 1;
  static final int SINUS = 2;
  static final int COSINUS = 3;
  static final int NATURAL_LOGARITHM = 4;
  static final int X_TO_POWER_OF_A = 5;
  static final int ARCTANGENS = 6;

  public static void main(String[] args) {
    int menuOption;
    String message;

    Printer.printGreeting();
    do {
      Printer.printMenu();
      message = "Enter number of a function: ";
      menuOption = Validator.validateNumber(EXIT, ARCTANGENS, message);
      switch (menuOption) {
        case EXIT:
          System.out.println("Exiting the program");
          Validator.scanner.close();
          break;
        case E_TO_POWER_OF_X:
          Functions.eToPowerX();
          break;
        case SINUS:
          Functions.Sinus();
          break;
        case COSINUS:
          Functions.cosinus();
          break;
        case NATURAL_LOGARITHM:
          Functions.naturalLogarithm();
          break;
        case X_TO_POWER_OF_A:
          Functions.xToPowerA();
          break;
        case ARCTANGENS:
          Functions.arctangens();
          break;
        default:
          System.out.println("No such option!");
      }
    } while (menuOption != EXIT);
  } 
}