package prakticheskaya13;

/**
 * Class for displaying information to a user.
 */
public class Printer {
  /**
   * Prints a greeting to a user and shows basic info about the program.
   */
  public static void printWelcome() {
    System.out.println("Добро пожаловать в программу для изучения английского языка!");
    System.out.println("Здесь вы можете читать статьи, тексты и проходить тесты!");
  }

  /**
   * Prints main menu of the program.
   */
  public static void printMenu() {
    System.out.println("\nВыберите опцию меню:");
    System.out.println("1. Прочитать статью");
    System.out.println("2. Прочитать текст");
    System.out.println("3. Пройти тест");
    System.out.println("\n0. Выйти из программы");
  }
}