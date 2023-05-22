package prakticheskaya13;

/**
 * Class for imitating a Database.
 */
public class DataBase {

  /**
   * Creates a {@code Text} object with data.

   * @return created {@code Text} object.
   */
  public static Text loadText() {
    String name = "My family";
    String text =  "I live in a house near the mountains. I have two brothers and one sister,\n"
        + "and I was born last. My father teaches mathematics, and my mother is a nurse at a\n"
        + "big hospital. My brothers are very smart and work hard in school. My sister is a\n"
        + "nervous girl, but she is very kind. My grandmother also lives with us. She came from\n"
        + "Italy when I was two years old. She has grown old, but she is still very strong.\n"
        + "She cooks the best food!\n\n"
        + "My family is very important to me. We do lots of things together. My brothers and\n"
        + "I like to go on long walks in the mountains. My sister likes to cook with my\n"
        + "grandmother. On the weekends we all play board games together.\n"
        + "We laugh and always have a good time. I love my family very much.\n";
    String question = "Ответьте на вопрос по тексту: \n'On the weekends we are ___':";
    String[] answers = {"walk in the mountains", "cook together", "play board games",
        "go to a movie"};
    String correctAnswer = "play board games";
    Test test = new Test(question, answers, correctAnswer);
    return new Text(name, text, test);
  }

  /**
   * Creates a {@code Test} object with data.

   * @return created {@code Test} object.
   */
  public static Test loadTest() {
    String question = "Вставьте правильную форму глагола to be:\nWe '___' seeing him yesterday";
    String[] answers = {"are", "is", "were", "was"};
    String correctAnswer = "were";
    return new Test(question, answers, correctAnswer);
  }

  /**
   * Creates am {@code Article} object with data.

   * @return created {@code Article} object.
   */
  public static Article loadArticle() {
    String name = "Артикли A, An, The в английском языке.";
    String text = "В английском языке артикли 'a' и 'an' используются, когда говорят об одиночных\n"
        + "неопределенных (либо неизвестных говорящим) предметах, т.е. существительных.\n\n"
        + "'A' используется, если первый звук существительного является согласным, например:\n"
        + "'a table' - 'стол'; 'a doctor' - 'врач/доктор'; 'a computer' - 'компьютер'.\n\n"
        + "'An' используется, если первый звук существительного является гласным, например:\n"
        + "'an elephant' - 'слон'; 'an apple' - 'яблоко'; 'an architect' - 'архитектор'.\n\n"
        + "'The' используется, если говорится о каком-то конкретном предмете, например:\n"
        + "'show me the pen that bought yesterday'  - 'покажи мне ручку, которую ты купил вчера'.\n"
        + "Здесь важно, что говорящий говорит не о любой ручке, а о вполне определенной и\n"
        + "конкретной (которую собеседник купил вчера).\n"
        + "'The' может использоваться как с существительными единственного числа, "
        + "так и множественного.";
    String question = "Выберите правильный артикль:\n I want to eat '__' egg";
    String[] answers = {"a", "an", "the"};
    String correctAnswer = "an";
    Test test = new Test(question, answers, correctAnswer);
    return new Article(name, text, test);
  }
}
