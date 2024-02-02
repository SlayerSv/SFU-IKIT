package prakticheskaya1;

/**
 * Parent class of all rooms, contains overrided methods of equals, toString and
 * hash code.
 */
public class Room {
    static final int MINIMUM_ROOM_AREA = 10;
    static final int MAXIMUM_ROOM_AREA = 1000;

    private String adress;
    private int area;

    public Room() {
        this.adress = "Unknown adress";
        this.area = MINIMUM_ROOM_AREA;
    }

    /**
     * Custom constructor with parameters, checks that user enters values in certain
     * range.
     * If the value is not in a certain range, sets it to defined constant.
     * 
     * @param adress Adress of the created room.
     * @param area   Area of the created room in square meters.
     */
    public Room(String adress, int area) {
        this.adress = adress;
        if (area < MINIMUM_ROOM_AREA || area > MAXIMUM_ROOM_AREA) {
            System.out.println("Room area must be between " + MINIMUM_ROOM_AREA
                    + " and " + MAXIMUM_ROOM_AREA + ". Area is set to " + MINIMUM_ROOM_AREA);
            this.area = MINIMUM_ROOM_AREA;
        } else {
            this.area = area;
        }
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getArea() {
        return area;
    }

    /**
     * Sets area of the room, makes sure it`s in a certain range. If not, sets it to
     * defined constant.
     * 
     * @param area Area of the room in square meters.
     */
    public void setArea(int area) {
        if (area < MINIMUM_ROOM_AREA || area > MAXIMUM_ROOM_AREA) {
            System.out.println("Room area must be between " + MINIMUM_ROOM_AREA
                    + " and " + MAXIMUM_ROOM_AREA + ". Area is set to " + MINIMUM_ROOM_AREA);
            this.area = MINIMUM_ROOM_AREA;
        } else {
            this.area = area;
        }
    }

    /**
     * Overrided equals function, checks if compared object has the same class and
     * fields.
     * 
     * @param object Compared object.
     */
    @Override
    public boolean equals(Object object) {
        boolean isEqual;
        if (object == null || this.getClass() != object.getClass()) {
            isEqual = false;
        } else if (this == object) {
            isEqual = true;
        } else {
            Room comparedRoom = (Room) object;
            if (this.adress.toLowerCase().equals(comparedRoom.getAdress().toLowerCase())
                    && this.area == comparedRoom.getArea()) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return adress.hashCode() + area;
    }

    @Override
    public String toString() {
        return "Type: Room\nAdress: " + this.adress + "\nArea: " + this.area + "\n";
    }
}