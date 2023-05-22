package prakticheskaya13;

/**
 * Class for creating test questions. Holds question, possible answers, and correct answer.
 */
public class Test {
  private String question;
  private String[] answers;
  private String correctAnswer;

  /**
   * Default constructor, creates an object with "unknown" fields.
   */
  public Test() {
    this.question = "unknown";
    this.answers = new String[0];
    this.correctAnswer = "unknown";
  }

  /**
   * Constructor with arguments, creates an object with passed arguments.

   * @param question Question that needs to be answered.
   * @param answers All possible answers to choose from.
   * @param correctAnswer The correct answer to the question.
   */
  public Test(String question, String[] answers, String correctAnswer) {
    this.question = question;
    this.answers = answers;
    this.correctAnswer = correctAnswer;
  }

  public String getQuestion() {
    return question;
  }

  public String[] getAnswers() {
    return answers;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public boolean checkCorrectAnswer(String answer) {
    return answer == correctAnswer;
  }

  /**
   * Prints the question and runs the {@code printAnswers()} function to print answers.
   */
  public void print() {
    System.out.println("\n" + question);
    printAnswers();
  }

  /**
   * Prints all possible answers to a user, adds an index to each answer, starting with "1".
   */
  public void printAnswers() {
    for (int i = 0; i < answers.length; i++) {
      System.out.println(i + 1 + ". " + answers[i]);
    }
  }
}