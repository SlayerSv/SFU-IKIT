package prakticheskaya9;

/**
 * Gym room extends Room, an object that represents a gym room.
 * has overrided methods of equals, toString and hash code.
 */
public class GymRoom extends Room {
  static final int MINIMUM_TRAINEES = 1;
  static final int MAXIMUM_TRAINEES = 150;

  private String sportName;
  private int numberOfTrainees;
  
  /**
   * Default constructor, used to create a gym room with unknown values, uses super method
   * to call parent`s Room class` default constructor.
   */
  public GymRoom() {
    super();
    sportName = "Unknown sport name";
    numberOfTrainees = MINIMUM_TRAINEES;
  }

  /**
   * Custom constructor to create a gym room with known values. Calls parent`s constructor
   * of the class Room. Checks values to be in a certain range, if they are not, sets them to
   * minimal value defined by a constant.
   * @param adress Adress of the gym room.
   * @param area Area of the gym room in square meters.
   * @param sportName Name of the sport that is practiced in this gym room.
   * @param numberOfTrainees Number of people that can exercise in this gym room.
   */
  public GymRoom(String adress, int area, String sportName, int numberOfTrainees) {
    super(adress, area);
    this.sportName = sportName;
    if (numberOfTrainees < MINIMUM_TRAINEES
        || numberOfTrainees > MAXIMUM_TRAINEES) {
      System.out.println("Number of Trainees must be between "
          + MINIMUM_TRAINEES + " and " + MAXIMUM_TRAINEES
          + ". Number of trainees is set to " + MINIMUM_TRAINEES);
      this.numberOfTrainees = MINIMUM_TRAINEES;
    } else {
      this.numberOfTrainees = numberOfTrainees;
    }
  }

  public String getSportName() {
    return sportName;
  }

  public void setSportName(String sportName) {
    this.sportName = sportName;
  }

  public int getNumberOfTrainees() {
    return numberOfTrainees;
  }

  /**
   * Sets the number of people that can exercise in this gym room. Checks that values are
   * in a certain range, if they are not, sets them to minimal value defined by a constant.
   * @param numberOfTrainees Number of people that can exercise in this gym room.
   */
  public void setNumberOfTrainees(int numberOfTrainees) {
    if (numberOfTrainees < MINIMUM_TRAINEES
        || numberOfTrainees > MAXIMUM_TRAINEES) {
      System.out.println("Number of Trainees must be between "
          + MINIMUM_TRAINEES + " and " + MAXIMUM_TRAINEES
          + ". Number of trainees is set to " + MINIMUM_TRAINEES);
      this.numberOfTrainees = MINIMUM_TRAINEES;
    } else {
      this.numberOfTrainees = numberOfTrainees;
    }
  }

  /**
   * Overrided equals function, checks if compared object has the same class and fields.
   */
  @Override
  public boolean equals(Object object) {
    boolean isEqual;
    if (!super.equals(object)) {
      isEqual = false;
    } else {
      GymRoom comparedGym = (GymRoom) object;
      if (this.sportName.equals(comparedGym.getSportName())
          && this.numberOfTrainees == comparedGym.getNumberOfTrainees()) {
        isEqual = true;   
      } else {
        isEqual = false;
      }
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.sportName.hashCode() + numberOfTrainees;
  }

  @Override
  public String toString() {
    return "Type: Gym room\nAdress: " + this.getAdress() + "\nArea: " + this.getArea()
        + "\nSport: " + this.sportName + "\nNumber of trainees: " + this.numberOfTrainees;
  }
}