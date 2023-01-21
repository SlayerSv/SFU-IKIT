package prakticheskaya8;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Client that will visit the Barbershop to get a haircut.
 */
public class Client implements Runnable {

  /**
   * Counter to name clients and track how many clients has been created. Uses {@code Atomiclong}
   * to ensure that multiple running {@code Threads} properly get access to it.
   */
  public static AtomicLong nextClientNumber = new AtomicLong();

  private final long clientNumber;

  Barbershop barbershop;
  private boolean isCut = false;

  public void setIsCut(boolean isCut) {
    this.isCut = isCut;
  }

  public boolean getIsCut() {
    return isCut;
  }

  public Client() {
    clientNumber = nextClientNumber.incrementAndGet();
  }

  public Client(Barbershop barbershop) {
    clientNumber = nextClientNumber.incrementAndGet();
    this.barbershop = barbershop;
  }

  public String getName() {
    return "Client " + clientNumber;
  }

  /**
   * Client walks into Barbershop to get a haircut. If the barbershop is not full, gets the haircut
   * and leaves the Barbershop. If it`s full, then the {@code IllegalStateException} is thrown,
   * after that Clients just walks away from the Barbershop.
   */
  @Override
  public void run() {
    try {
      barbershop.enterBarbershop(this);
      barbershop.leaveBarbershop(this);
    } catch (IllegalStateException e) {
      System.out.println("Waiting room is full. " + getName()
          + " leaves the barbershop without a haircut!");
    }
  }
}