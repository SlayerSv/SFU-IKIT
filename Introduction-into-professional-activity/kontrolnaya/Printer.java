package kontrolnaya;

public class Printer {

  public static void printGreeting() {
    System.out.println("Hello, welcome to the Math Functions Program.");
    System.out.println("Here, you can calculate different values for different functions.");
    System.out.println("To start, you need to choose a function by entering it`s number.");
    System.out.println("Then, enter values for that function to calculate it.");
    System.out.println("And after that, you need to choose an accuracy of calculations\n");
  }

  public static void printMenu() {
    System.out.println("Choose a function to calculate\n");
    System.out.println("1. e to the power of x");
    System.out.println("2. Sinus of x");
    System.out.println("3. Cosinus of x");
    System.out.println("4. Natural logarithm of x");
    System.out.println("5. X to the power of 'a'");
    System.out.println("6. Arctangens of x");
    System.out.println("\n0. Exit the program\n");
  }
}