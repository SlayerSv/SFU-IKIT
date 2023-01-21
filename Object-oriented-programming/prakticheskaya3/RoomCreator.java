package prakticheskaya3;

import java.util.ArrayList;

/**
 * Allows a user to create different type of rooms and add them to an ArrayList.
 * Shows menu to a user and accepts input and validates intered values.
 */
public class RoomCreator {
  static final int BACK = 0;
  static final int ROOM = 1;
  static final int CLASS_ROOM = 2;
  static final int OFFICE_ROOM = 3;
  static final int GYM_ROOM = 4;

  private String message;
  private String adress;
  private int area;
  ValueValidator validator = new ValueValidator();
  Printer printer = new Printer();
  

  /**
   * Shows a user menu to choose what type of room he wants to create, accepts input and
   * invokes different create methods of rooms based on it. Runs unfinite while loop until
   * user decides to go back to main menu.
   * @param roomsList ArrayList of rooms to which new room will be added.
   */
  public void chooseAndCreateRoom(ArrayList<Room> roomsList) {
    int input;
    do {
      printer.printCreateRoomMenu();
      message = "\nEnter the number of a room type: ";
      input = validator.validateNumber(BACK, GYM_ROOM, message);
      switch (input) {
        case BACK:
          System.out.println("Returning back to main menu");
          break;
        case ROOM:
          roomsList.add(createRoom());
          break;
        case CLASS_ROOM:
          roomsList.add(createClassRoom());
          break;
        case OFFICE_ROOM:
          roomsList.add(createOfficeRoom());
          break;
        case GYM_ROOM:
          roomsList.add(createGymRoom());
          break;
        default:
          System.out.println("\nNo such room type!");
      }
    } while (input != BACK);
  }

  /**
   * Creates a base room by accepting and validating values from a user. Makes sure that
   * entered values are in certain range.
   * @return Created room object.
   */
  public Room createRoom() {
    message = "Enter room`s adress: ";
    String adress = validator.validateString(message);
    message = "Enter room`s area in square meters: ";
    area = validator.validateNumber(Room.MINIMUM_ROOM_AREA, Room.MAXIMUM_ROOM_AREA, message);
    Room newRoom = new Room(adress, area);
    System.out.println("\nNew room has been created!");
    return newRoom;
  }

  /**
   * Creates a classroom by accepting and validating values from a user. Makes sure that
   * entered values are in certain range.
   * @return Created classroom object.
   */
  public ClassRoom createClassRoom() {
    message = "Enter classroom`s adress: ";
    adress = validator.validateString(message);
    message = "Enter classroom`s area in square meters: ";
    area = validator.validateNumber(Room.MINIMUM_ROOM_AREA, Room.MAXIMUM_ROOM_AREA, message);
    message = "Enter what subject is studied in this classroom: ";
    String subject = validator.validateString(message);
    message = "Enter how many students can study in this classroom: ";
    int numberOfStudents = validator.validateNumber(ClassRoom.MINIMUM_NUMBER_OF_STUDENTS,
                                                    ClassRoom.MAXIMUM_NUMBER_OF_STUDENTS,
                                                    message);
    ClassRoom newClassRoom = new ClassRoom(adress, area, subject, numberOfStudents);
    System.out.println("\nNew classroom has been created!");
    return newClassRoom;
  }

  /**
   * Creates an office room by accepting and validating values from a user. Makes sure that
   * entered values are in certain range.
   * @return Created office room object.
   */
  public OfficeRoom createOfficeRoom() {
    message = "Enter office room`s adress: ";
    adress = validator.validateString(message);
    message = "Enter office room`s area in square meters: ";
    area = validator.validateNumber(Room.MINIMUM_ROOM_AREA, Room.MAXIMUM_ROOM_AREA, message);
    message = "Enter the name of the department this office room belongs to: ";
    String department = validator.validateString(message);
    message = "Enter how many people work in this office room: ";
    int numberOfWorkers = validator.validateNumber(OfficeRoom.MINIMUM_NUMBER_OF_WORKERS,
                                                   OfficeRoom.MAXIMUM_NUMBER_OF_WORKERS,
                                                   message);
    OfficeRoom newOfficeRoom = new OfficeRoom(adress, area, department, numberOfWorkers);
    System.out.println("\nNew office room has been created!");
    return newOfficeRoom;
  }

  /**
   * Creates a gym room by accepting and validating values from a user. Makes sure that
   * entered values are in certain range.
   * @return Created gym room object.
   */
  public GymRoom createGymRoom() {
    message = "Enter gym room`s adress: ";
    adress = validator.validateString(message);
    message = "Enter gym room`s area in square meters: ";
    area = validator.validateNumber(Room.MINIMUM_ROOM_AREA, Room.MAXIMUM_ROOM_AREA, message);
    message = "Enter what kind of sport is practiced in this gym room: ";
    String sportName = validator.validateString(message);
    message = "Enter how many people can exercise in this gym room: ";
    int numberOfTrainees = validator.validateNumber(GymRoom.MINIMUM_NUMBER_OF_TRAINEES,
                                                    GymRoom.MAXIMUM_NUMBER_OF_TRAINEES,
                                                    message);
    GymRoom newGymRoom = new GymRoom(adress, area, sportName, numberOfTrainees);
    System.out.println("\nNew gym room has been created!");
    return newGymRoom;
  }
}