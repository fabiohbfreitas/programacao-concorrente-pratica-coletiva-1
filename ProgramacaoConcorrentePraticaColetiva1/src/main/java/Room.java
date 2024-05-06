import java.util.ArrayList;
import java.util.List;

public class Room {
    String name;
    List<Guest> guests;

    public Room(String id) {
        this.name = id;
        this.guests = new ArrayList<>();
    }
}
