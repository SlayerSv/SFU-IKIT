package prakticheskaya3;

/**
 * Office room extends Room, an object that represents an office room.
 * has overrided methods of equals, toString and hash code.
 */
public class OfficeRoom extends Room {
  static final int MINIMUM_WORKERS = 1;
  static final int MAXIMUM_WORKERS = 200;

  private String department;
  private int numberOfWorkers;

  /**
   * Default constructor, used to create an office room with unknown values,
   * uses super method to call parent`s Room class` default constructor.
   */
  public OfficeRoom() {
    super();
    department = "Unknown department";
    numberOfWorkers = MINIMUM_WORKERS;
  }

  /**
   * Custom constructor to create an office room with known values. Calls parent`s constructor
   * of the class Room. Checks values to be in a certain range, if they are not, sets them to
   * minimal value defined by a constant.
   * @param adress Adress of the office room.
   * @param area Area of the office room in square meters.
   * @param department Name of the department, to which this office belongs.
   * @param numberOfWorkers Number of workers that can work in this office room.
   */
  public OfficeRoom(String adress,
                    int area,
                    String department, 
                    int numberOfWorkers) {
    super(adress, area);
    this.department = department;
    if (numberOfWorkers < MINIMUM_WORKERS
        || numberOfWorkers > MAXIMUM_WORKERS) {
      System.out.println("Number of workers must be between "
          + MINIMUM_WORKERS + " and " + MAXIMUM_WORKERS
          + ". Number of workers is set to " + MINIMUM_WORKERS);
      this.numberOfWorkers = MINIMUM_WORKERS;
    } else {
      this.numberOfWorkers = numberOfWorkers;
    }                
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public int getNumberOfWorkers() {
    return numberOfWorkers;
  }

  /**
   * Sets the number of workers that can work in this office room. Checks that values are
   * in a certain range, if they are not, sets them to minimal value defined by a constant.
   * @param numberOfWorkers Number of workers that can work in this office room.
   */
  public void setNumberOfWorkers(int numberOfWorkers) {
    if (numberOfWorkers < MINIMUM_WORKERS
        || numberOfWorkers > MAXIMUM_WORKERS) {
      System.out.println("Number of workers must be between "
          + MINIMUM_WORKERS + " and " + MAXIMUM_WORKERS
          + ". Number of workers is set to " + MINIMUM_WORKERS);
      this.numberOfWorkers = MINIMUM_WORKERS;
    } else {
      this.numberOfWorkers = numberOfWorkers;
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
      OfficeRoom comparedOffice = (OfficeRoom) object;
      if (this.department.equals(comparedOffice.getDepartment())
          && this.numberOfWorkers == comparedOffice.getNumberOfWorkers()) {
        isEqual = true;   
      } else {
        isEqual = false;
      }
    }
    return isEqual;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.department.hashCode() + numberOfWorkers;
  }

  @Override
  public String toString() {
    return "Type: Office room\nAdress: " + this.getAdress() + "\nArea: " + this.getArea()
        + "\nDepartment: " + this.department + "\nNumber of workers: " + this.numberOfWorkers;
  }
}