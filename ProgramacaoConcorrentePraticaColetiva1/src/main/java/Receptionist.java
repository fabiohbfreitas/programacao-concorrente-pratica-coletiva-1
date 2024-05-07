import java.util.List;

public class Receptionist extends Thread {
    Hotel hotel;

    public Receptionist(String name, Hotel hotel) {
        super(name);
        this.hotel = hotel;
    }

    public void giveKeys(Room room, Family family) {
        room.guests = (family.members);
        family.currentRoom = room;
        hotel.occupiedRooms.add(room);
    }

    @Override
    public void run() {
        while (!hotel.finished.get()) {
            try {
//                System.out.println(this.getName() + " checking wait queue");
                hotel.checkRoomAndGuest(this);
                Thread.sleep(400);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
