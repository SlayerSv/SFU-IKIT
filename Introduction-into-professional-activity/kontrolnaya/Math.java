package kontrolnaya;

public class Math {
  public static double factorialOf(int number) {
    double factorial = 1;
    for (int i = 1; i <= number; i++) {
      factorial = factorial * i;
    }
    return factorial;
  }

  public static double powerOf(int number, int power) {
    if (power == 0) {
      return 1;
    }
    double powerOfNumber = number;
    for (int i = 2; i <= power; i++) {
      powerOfNumber = powerOfNumber * number;
    }
    return powerOfNumber;
  }

  public static double powerOf(double number, int power) {
    if (power == 0) {
      return 1;
    }
    double powerOfNumber = number;
    for (int i = 2; i <= power; i++) {
      powerOfNumber = powerOfNumber * number;
    }
    return powerOfNumber;
  }
}
