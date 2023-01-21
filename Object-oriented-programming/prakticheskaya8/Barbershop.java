package prakticheskaya8;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Class that simulates a barbershop, made for interacting with multiple Threads. Uses
 * various form of synchronization to make sure proper usage of its resources.
 */
public class Barbershop {

  ArrayBlockingQueue<Barber> availableBarbers = new ArrayBlockingQueue<>(10);
  /**Array that represents chairs in the waiting room of the barbershop.*/
  ArrayBlockingQueue<Client> queue;
  /**Array that represents a working chair of a barber. Can hold Clients and a Barber itself. */
  //ArrayBlockingQueue<Runnable> workingChairs = new ArrayBlockingQueue<>(NUMBER_OF_BARBERS);

  public Barbershop() {
    queue = new ArrayBlockingQueue<>(4);
  }

  public Barbershop(int numberOfChairs) {
    queue = new ArrayBlockingQueue<>(numberOfChairs);
  }

  /**
   * Method checks if there are available Barbers to do a haircut. If there is, he retrieves and
   * removes that Barber from {@code availableBarbers} list and returns him to a caller.
   * 
   * <p>If there are no available Barbers, then returns {@code null}.
   */
  public synchronized Barber getAvailableBarber() {
    if (availableBarbers.peek() != null) {
      Barber availableBarber = availableBarbers.poll();
      return availableBarber;
    }
    return null;
  }

  /**
   * Barber releases a client by notifying him, so the customer knows that he can leave
   * the barbershop. Untill that, clients waits until his haircut is done.
   */
  public synchronized void releaseCustomer() {
    notifyAll();
  }

  /**
   * After Barber is done cutting hair of a previous client, he checks if there are more clients
   * in the waiting room ({@code Array queue}). If there are clients, he calls the one that has
   * waited the longest (FIFO, head of the Array), and puts him in the working chair.
   * 
   * <p>If the Barber doesn`t see any clients in the waiting room, he sits in his own chair and
   * falls asleep.
   */
  public synchronized void callNextCustomer(Barber barber) {
    if (queue.peek() != null) {
      Client nextClient = queue.poll();
      System.out.println(barber.getName() + " calls " + nextClient.getName());
      barber.workingChair.add(nextClient);
      System.out.println(nextClient.getName() + " sits in the working chair of "
          + barber.getName());
    } else {
      barber.fallAsleep();
    }
  }

  /**
   * Client walks into Barbershop, if the are any Barbers sleeping, he wakes up the Barber who
   * sleeps for a longer time, removes him from his chair, sits there himself and waites
   * for a haircut.
   * 
   * <p>If the are no Barbers available Client tries to go to waiting room
   * and take a sit there (add himself to {@code Array queue }). If the array is full, it will throw
   * the {@code IllegalStateException}, which will be handled by Client {@code run()} method.
   * @param client  Client that walks into the Barbershop.
   */
  public synchronized void enterBarbershop(Client client) {
    System.out.println(client.getName() + " walked into barbershop");
    Barber availableBarber = getAvailableBarber();
    if (availableBarber != null) {
      System.out.println("Client sees" + availableBarber.getName() + " sleeping in his chair.");
      availableBarber.setIsSleeping(false);
      availableBarber.workingChair.poll();
      System.out.println(client.getName() + " wakes up " + availableBarber.getName()
          + " and removes him from his chair.");
      availableBarber.sitInWorkingChair(client);
    } else {
      System.out.println(client.getName() + " sees that all Barbers are busy, goes to the waiting"
          + "room");
      queue.add(client);
      System.out.println(client.getName() + " sees a free chair in the waiting room, takes a sit ");
    }
  }

  /**
   * After Client sits in the working chair, he waits until the Barber is done cutting his hair
   * (until {@code isCut} turns true, set by the Barber when he finishes cutting hair).
   * Gets notified by the Barber after he finishes his work in {@code releasClient()} method.
   * @param client Client that will leave the Barbershop after the haircut.
   */
  public synchronized void leaveBarbershop(Client client) {
    while (!client.getIsCut()) {
      try {
        wait();
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
    System.out.println(client.getName() + " leaves the barbershop with a great haircut!");
  }
}