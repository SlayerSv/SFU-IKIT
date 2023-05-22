package prakticheskaya13;

/**
 * Class for communicating between classes and a user.
 */
public class Controller {

  public static void loadAndRunArticle() {
    Article article = DataBase.loadArticle();
    runArticle(article);
  }

  public static void runArticle(Article article) {
    article.print();
    runTest(article.getTest());
  }

  public static void loadAndRunText() {
    Text text = DataBase.loadText();
    runText(text);
  }

  public static void runText(Text text) {
    text.print();
    runTest(text.getTest());
  }

  public static void loadAndRunTest() {
    Test test = DataBase.loadTest();
    runTest(test);
  }
  
  /**
   * PRints the test question and asnwers to a user, then asks for an answer.
   * Checks if the user's answer is correct.

   * @param test Test question.
   */
  public static void runTest(Test test) {
    test.print();
    String message = "\nВведите правильный ответ: ";
    int userInput = Validator.getValidator().validateInt(1, test.getAnswers().length,
        message);
    String userAnswer = test.getAnswers()[userInput - 1];
    if (test.checkCorrectAnswer(userAnswer)) {
      System.out.println("\nВаш ответ правильный!");
    } else {
      System.out.println("\nВаш ответ неправильный :(");
      System.out.println("Правильный ответ: '" + test.getCorrectAnswer() + "'");
    }
  }
}
