package PR1;

/**
 * Classroom extends Room, an object that represents a classroom.
 * has overrided methods of equals, toString and hash code.
 */
public class ClassRoom extends Room {
    static final int MINIMUM_STUDENTS = 1;
    static final int MAXIMUM_STUDENTS = 120;

    private String subject;
    private int numberOfStudents;

    /**
     * Default constructor, used to create a classroom with unknown values, uses
     * super method to call
     * parent`s Room class` default constructor.
     */
    public ClassRoom() {
        super();
        subject = "Unknown subject";
        numberOfStudents = MINIMUM_STUDENTS;
    }

    /**
     * Custom constructor to create a classroom with known values. Calls parent`s
     * constructor
     * of class Room. Checks values to be in a certain range, if they are not, sets
     * them to
     * minimal value defined by a constant.
     * 
     * @param adress           Adress of the classroom.
     * @param area             Area of the classroom in square meters.
     * @param subject          Subject that is studied in this classroom.
     * @param numberOfStudents Number of stundents that can study in this classroom.
     */
    public ClassRoom(String adress, int area, String subject, int numberOfStudents) {
        super(adress, area);
        this.subject = subject;
        if (numberOfStudents < MINIMUM_STUDENTS
                || numberOfStudents > MAXIMUM_STUDENTS) {
            System.out.print("Number of students must be between "
                    + MINIMUM_STUDENTS + " and " + MAXIMUM_STUDENTS
                    + ". Number of students is set to " + MINIMUM_STUDENTS + ".\n");
            this.numberOfStudents = MINIMUM_STUDENTS;
        } else {
            this.numberOfStudents = numberOfStudents;
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    /**
     * Sets number of students that can study in a classroom. Checks that values are
     * in a certain range, if they are not, sets them to minimal value defined by a
     * constant.
     * 
     * @param numberOfStudents Number of students that can study in this classroom.
     */
    public void setNumberOfStudents(int numberOfStudents) {
        if (numberOfStudents < MINIMUM_STUDENTS
                || numberOfStudents > MAXIMUM_STUDENTS) {
            System.out.print("Number of students must be between "
                    + MINIMUM_STUDENTS + " and " + MAXIMUM_STUDENTS
                    + ". Number of students is set to " + MINIMUM_STUDENTS + ".\n");
            this.numberOfStudents = MINIMUM_STUDENTS;
        } else {
            this.numberOfStudents = numberOfStudents;
        }
    }

    /**
     * Overrided equals function, checks if compared object has the same class and
     * fields.
     */
    @Override
    public boolean equals(Object object) {
        boolean isEqual;
        if (!super.equals(object)) {
            isEqual = false;
        } else {
            ClassRoom comparedClassRoom = (ClassRoom) object;
            if (this.subject.equals(comparedClassRoom.getSubject())
                    && this.numberOfStudents == comparedClassRoom.getNumberOfStudents()) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.subject.hashCode() + numberOfStudents;
    }

    @Override
    public String toString() {
        return "Type: Classroom\nAdress: " + this.getAdress() + "\nArea: " + this.getArea()
                + "\nSubject: " + this.subject + "\nNumber of students: " + this.numberOfStudents
                + ".\n";
    }
}