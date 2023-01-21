package prakticheskaya8;

import java.util.concurrent.Callable;

/**
 * Class to block {@code Executor} after he created a {@code Client Thread} so that they are
 * created in intervals in a while loop.
 */
public class Delayer implements Callable<Boolean> {
  
  @Override
  public Boolean call() throws Exception {
    return true;
  }
}
