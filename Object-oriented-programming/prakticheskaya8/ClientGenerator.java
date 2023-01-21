package prakticheskaya8;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class for creating {@code Client Threads} in a while loop with random intervals.
 */
public class ClientGenerator {
  public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
  static Random random = new Random();
  static int interval;

  /**
   * Takes a random interval in seconds and schedules creation of {@code Client Thread} after that
   * delay. After that creates callable task with the same delay and calls its {@code get()} method.
   * This is needed to block executor in a while loop from scheduling creation of another Client
   * Thread until last Client is actually created.
   * @param client Task that will be executed (represents a client).
   */
  public static void generateClient(Client client) {
    interval = random.nextInt(9);
    executor.schedule(client, interval, TimeUnit.SECONDS);
    Future<Boolean> future = executor.schedule(new Delayer(), interval, TimeUnit.SECONDS);
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      System.out.println(e);
    }
  }
}