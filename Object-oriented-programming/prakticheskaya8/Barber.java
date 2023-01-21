package prakticheskaya8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class that represents the Barber in the Barbershop that cuts hair of Clients.
 * Implements runnable.
 */
public class Barber implements Runnable {

  /**
   * Counter to name Barbers and track how many Barbers has been created. Uses {@code Atomiclong}
   * to ensure that multiple running {@code Threads} properly get access to it.
   */
  private static AtomicLong nextBarber = new AtomicLong();

  private final String name;
  /**Value to determine how long the Barber will cut hair of a Client. */
  private final int haircutTime;
  private final Barbershop barbershop;
  /**Value that says if the Barber is currently sleeping. */
  private boolean isSleeping = true;
  public ArrayBlockingQueue<Runnable> workingChair = new ArrayBlockingQueue<>(1);

  /**Default constructor without arguments. Sets the haircut time to a default value.
   * Increments the Barbers counter to track Barbers and give them names. Creates a new barbershop
   * object.
   */
  public Barber() {
    this.name = "Barber " + nextBarber.incrementAndGet();
    this.barbershop = new Barbershop();
    this.haircutTime = 4;
  }

  /**
   * Constructor with arguments. Sets the haircut time to a passed value. Increments the Barbers
   * counter to track Barbers and give them names. Sets the barbershop to a passed barbershop
   * object.
   */
  public Barber(Barbershop barbershop, int haircutTime) {
    this.name = "Barber " + nextBarber.incrementAndGet();
    this.barbershop = barbershop;
    this.haircutTime = haircutTime;
  }

  public String getName() {
    return name;
  }

  public boolean getIsSleeping() {
    return isSleeping;
  }

  public void setIsSleeping(boolean isSleeping) {
    this.isSleeping = isSleeping;
  }

  /**
   * Barber waits until a Client sits in his working chair to get a haircut. After client sits in
   * the working chair he notifies the Barber so he can start cutting hair.
   */
  public synchronized void waitCustomers() {
    if (workingChair.peek() == null || workingChair.peek().getClass() == this.getClass()) {
      try {
        wait();
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }

  /**
   * If the Barber doesnt see any customers in the waiting room, he sits in his chair, falls asleep,
   * adds himself to the {@code Queue availableBarbers}, so the Barbershop class can track if there
   * are availabe barbers to do a haircut.
   */
  public synchronized void fallAsleep() {
    System.out.println(getName() + " doesn`t see any customers in the waiting room");
    workingChair.add(this);
    setIsSleeping(true);
    System.out.println(getName() + " falls asleep in his chair...");
    barbershop.availableBarbers.add(this);
  }

  /**
   * Client sits in the working chair of the waiting Barber and notifies him, so the Barber can
   * start doing haircut.
   * @param client Client that will recieve a haircut from the Barber.
   */
  public synchronized void sitInWorkingChair(Client client) {
    workingChair.add(client);
    System.out.println(client.getName() + " sits in working chair of " + getName());
    notifyAll();
  }

  /**
   * Barber cuts hair of a client for a duration of {@code haircitTime}, sets the client`s
   * {@code isCut} to true, (condition for the client to leave the barbershop), then removes
   * the client from the working chair.
   */
  public synchronized void cutHair() {
    try {
      Client client = (Client) workingChair.peek();
      System.out.println(getName() + " starts cutting hair of " + client.getName());
      Thread.sleep(haircutTime * 1000);
      client.setIsCut(true);
      System.out.println(getName() + " is done cutting hair of " + client.getName());
      workingChair.poll();
      System.out.println(client.getName() + " stands from the working chair of " + this.getName());
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }

  /**
   * Initially Barber is sleeping in his own working chair and waits for new clients. After client
   * sits in his chair, he cuts his hair, releases the client, checks the waiting room for other
   * clients. Repeats the cycle indefinitely.
   */
  @Override
  public void run() {
    barbershop.availableBarbers.add(this);
    System.out.println(getName() + " is sleeping in his chair...");
    while (true) {
      waitCustomers();
      cutHair();
      barbershop.releaseCustomer();
      barbershop.callNextCustomer(this);
    }
  }
}