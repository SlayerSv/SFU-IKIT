package prakticheskaya8;

/**
 * Class for simulating a sleeping barber problem.
 */
public class Prakticheskaya8 {

  /**
   * Indefinitely Creates {@code Threads} with random intervals simulating clients visiting
   * a barbershop. Takes 2 parameters from a user: 1) time to do a haircut, and
   * 2) size of the array representing chairs in the waiting room for clients.
   * After that executes {@code Threads} simulating barber and clients.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    System.out.println("Welcome to the Barbershop simulation");
    System.out.println("Here, multiple clients will visit the barbershop to get a haircut");
    System.out.println("Barber will serve them or sleep if there are no clients");
    System.out.println("To start, enter some values\n");
    String message = "";
    message = "Enter haircut time (1-20 seconds): ";
    int haircutTime = Validator.validateInt(1, 20, message);
    message = "Enter number of chairs in the waiting room (1-5): ";
    int numberOfChairs = Validator.validateInt(1, 5, message);
    Barbershop barbershop = new Barbershop(numberOfChairs);
    Barber barber; 

    for (int i = 0; i < 2; i++) {
      barber = new Barber(barbershop, haircutTime);
      ClientGenerator.executor.execute(barber);
    }
    while (true) {
      ClientGenerator.generateClient(new Client(barbershop));
    }
  }
}