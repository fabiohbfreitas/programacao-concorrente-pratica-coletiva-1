import java.util.ArrayList;
import java.util.List;

public class Room {
    String name;
    List<Guest> guests;
    static final int MAX_GUESTS = 4;

    public Room(String id) {
        this.name = id;
        this.guests = new ArrayList<>();
    }
}
