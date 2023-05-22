package prakticheskaya13;

/**
 * Class for creating texts. Holds name of the text, text itself and a test question.
 */
public class Text {
  private String name;
  private String text;
  private Test test;

  /**
   * Default constructor. Creates an object with "unknown" fields.
   */
  public Text() {
    this.name = "unknown";
    this.text = "unknown";
    this.test = new Test();
  }

  /**
   * Constructor with arguments, creates a text object with passed arguments.

   * @param name Name of the text.
   * @param text The body of the text.
   * @param test Test question for the text.
   */
  public Text(String name, String text, Test test) {
    this.name = name;
    this.text = text;
    this.test = test;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Test getTest() {
    return test;
  }

  public void setTest(Test test) {
    this.test = test;
  }

  /**
   * Prints the name of the text and it's body on the screen.
   */
  public void print() {
    System.out.println("\n" + name.toUpperCase() + "\n");
    System.out.println(text);
  }
}