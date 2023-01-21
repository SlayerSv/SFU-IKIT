package prakticheskaya4;

/**functional interface for checking values. Must be implemented in a class.*/
@FunctionalInterface
public interface Matchable {
  boolean match(int number1, int number2);
}