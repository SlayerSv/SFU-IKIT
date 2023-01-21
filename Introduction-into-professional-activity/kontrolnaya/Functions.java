package kontrolnaya;

public class Functions {
  static final double MIN_ACCURACY = 0.000_000_1;
  static final double MAX_ACCURACY = 0.01;
  static final double LN_MIN_VALUE = 0.01;
  static final double LN_MAX_VALUE = 2.0;
  static final double X_MIN_VALUE = 0.01;
  static final double X_MAX_VALUE = 1.99;
  static final double PI = 3.14159265359;
  static final double FULL_CIRCLE = PI * 2;
  static final double ARCTANGENS_MIN_VALUE = -1.0;
  static final double ARCTANGENS_MAX_VALUE = 1.0;

  private static String message;
  
  public static void eToPowerX() {
    message = "Enter power of e: ";
    double powerOfe = Validator.validateNumber(message);
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; "
        + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double result = 0;
    double n = 1;
    for (int i = 0; n > accuracy; i++) {
      n = Math.powerOf(powerOfe, i) / Math.factorialOf(i);
      result += n;
      System.out.println("e in power of " + powerOfe + " is " + result);
    }
  }

  public static void Sinus() {
    message = "Enter value to calculate sinus: ";
    double sinusValue = Validator.validateNumber(message);
    double sinusFormulaValue = sinusValue;
    if (sinusFormulaValue > FULL_CIRCLE || sinusFormulaValue < FULL_CIRCLE) {
      sinusFormulaValue = sinusFormulaValue % FULL_CIRCLE;
    }
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; "
        + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double sinusResult = 0;
    double n = 1;
    for (int i = 1; n > accuracy; i++) {
      n = Math.powerOf(-1, i + 1) * Math.powerOf(sinusFormulaValue, 2 * i - 1)
          / Math.factorialOf(2 * i - 1);
      sinusResult += n;
      System.out.println("Sinus of " + sinusValue + " is " + sinusResult);
      if (n < 0) {
        n = -n;
      }
    }
  }

  public static void cosinus() {
    message = "Enter value to calculate cosinus: ";
    double cosinusValue = Validator.validateNumber(message);
    double cosinusFormulaValue = cosinusValue;
    if (cosinusFormulaValue > FULL_CIRCLE || cosinusFormulaValue < FULL_CIRCLE) {
      cosinusFormulaValue = cosinusFormulaValue % FULL_CIRCLE;
    }
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; "
        + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double cosinusResult = 0;
    double n = 1;
    for (int i = 0; n > accuracy; i++) {
      n = Math.powerOf(-1, i) * Math.powerOf(cosinusFormulaValue, 2 * i)
          / Math.factorialOf(2 * i);
      cosinusResult += n;
      System.out.println("Cosinus of " + cosinusValue + " is " + cosinusResult);
      if (n < 0) {
        n = -n;
      }
    }
  }

  public static void naturalLogarithm() {
    message = "Enter value to calculate natural logarithm ("
        + LN_MIN_VALUE + "; " + LN_MAX_VALUE + "): ";
    double lnValue = Validator.validateNumber(LN_MIN_VALUE, LN_MAX_VALUE, message);
    double lnXValue = lnValue - 1;
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; "
        + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double lnResult = 0;
    double n = 1;
    for (int i = 1; n > accuracy; i++) {
      n = Math.powerOf(-1, i + 1) * Math.powerOf(lnXValue, i) / i;
      lnResult += n;
      System.out.println("Natural logarithm of " + lnValue + " is " + lnResult);
      if (n < 0) {
        n = -n;
      }
    }
  }

  public static void xToPowerA() {
    message = "Enter 'x' value to calculate 'x' to power 'a' ("
        + X_MIN_VALUE + "; " + X_MAX_VALUE + "): ";
    double xvalue = Validator.validateNumber(X_MIN_VALUE, X_MAX_VALUE, message);
    double xformulaValue = xvalue - 1;
    message = "Enter 'a' value to calculate 'x' to power 'a': ";
    double avalue = Validator.validateNumber(message);
    double aformulaValue = 1;
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; " 
        + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double xresult = 1;
    double n = 1;
    for (int i = 1; n > accuracy; i++) {
      aformulaValue = aformulaValue * (avalue - i + 1);
      n = aformulaValue / Math.factorialOf(i) * Math.powerOf(xformulaValue, i);
      xresult += n;
      System.out.println(xvalue + " to power of " + avalue + " is " + xresult);
      if (n < 0) {
        n = -n;
      }
    }
  }

  public static void arctangens() {
    message = "Enter value to calculate arctangens ["
        + ARCTANGENS_MIN_VALUE + "; " + ARCTANGENS_MAX_VALUE + "]: ";
    double arctangensValue = Validator.validateNumber(ARCTANGENS_MIN_VALUE,
                                                      ARCTANGENS_MAX_VALUE,
                                                      message);
    message = "Enter accuracy of calculations [" + MIN_ACCURACY + "; " + MAX_ACCURACY + "]: ";
    double accuracy = Validator.validateNumber(MIN_ACCURACY, MAX_ACCURACY, message);
    double arctangensResult = 0;
    double n = 1;
    for (int i = 0; n > accuracy; i++) {
      n = Math.powerOf(-1, i) * Math.powerOf(arctangensValue, 2 * i + 1)
          / (2 * i + 1);
      arctangensResult += n;
      System.out.println("arctangens of " + arctangensValue + " is " + arctangensResult);
      if (n < 0) {
        n = -n;
      }
    }
  }
}