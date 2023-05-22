package prakticheskaya13;

/**
 * Class for creating articles. Extends the {@code Text} class.

 * @see Text
 */
public class Article extends Text {
  
  /**
   * Default constructor, creates an object with "unknown" fields.
   */
  public Article() {
    super();
  }

  /**
   * Constructor with arguments, creates a text object with passed arguments.

   * @param name Name of the article.
   * @param text The body of the article.
   * @param test Test question for the article.
   */
  public Article(String name, String text, Test test) {
    super(name, text, test);
  }
}
