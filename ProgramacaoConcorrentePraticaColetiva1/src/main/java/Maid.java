import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class Maid extends Thread{
    Hotel hotel;

    public Maid(String name,Hotel hotel) {
        super(name);
        this.hotel = hotel;
    }

    public void cleanRoom(Room room, Queue<Room> available) {
        try {
            System.out.println(this.getName() + " is cleaning " + room.name);
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
            if(!hotel.occupiedRooms.contains(room)){ // if the room is occupied and dirty the guest is out
                available.add(room);
                System.out.println(this.getName() + " finished cleaning " + room.name);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!hotel.finished.get()) {
            try {
                hotel.checkDirtyRooms(this);
                Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(600));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
