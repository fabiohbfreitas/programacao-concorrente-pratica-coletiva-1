import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hotel {
    Lock lock;
    Queue<Family> waitQueue;
    int complaints;
    Queue<Room> availableRooms;
    List<Room> occupiedRooms;
    Queue<Room> awaitingCleaning;

    public AtomicBoolean finished = new AtomicBoolean(false);

    public Hotel() {
        lock = new ReentrantLock(true);
        waitQueue = new LinkedList<>();
        complaints = 0;
        availableRooms = createRooms();
        occupiedRooms = new ArrayList<>();
        awaitingCleaning = new LinkedList<>();

        startReceptionists();
        startMaids();
    }

    private void startMaids() {
        for (int n = 0; n < 2; n++) {
            var maid = new Maid("Maid "+ n, this);
            maid.start();
        }
    }

    private void startReceptionists() {
        for (int n = 0; n < 2; n++) {
            var r = new Receptionist("Receptionist "+ n, this);
            r.start();
        }
    }

    public void arrive(Family family) {
        lock.lock();
        waitQueue.add(family);
        lock.unlock();
    }

    public boolean isWaiting(Family family) {
        lock.lock();
        if (waitQueue.contains(family)) {
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    public void leave(Family family) {
        lock.lock();
        waitQueue.remove(family);
        complaints += 1;
        System.out.println("Family " + family.familyID + " left a complaint.");
        lock.unlock();
    }

    private Queue<Room> createRooms() {
        var rooms = new LinkedList<Room>();
        for (int n = 0; n < 4; n++) {
            rooms.add(new Room("Room " + n));
        }
        return rooms;
    }

    public void checkRoomAndGuest(Receptionist receptionist) {
        lock.lock();
        if (!waitQueue.isEmpty() && !availableRooms.isEmpty()) {
            var family = waitQueue.poll();
            var room = availableRooms.poll();
            receptionist.giveKeys(room, family);
            System.out.println(receptionist.getName() + " gave the room " + room.name + " to family " + family.familyID);
        }
        lock.unlock();
    }


    public void checkout(Guest guest) {
        lock.lock();
        var guestRoom = guest.currentRoom;
        occupiedRooms.remove(guestRoom);
        awaitingCleaning.add(guestRoom);
        guest.currentRoom = null;
        System.out.println(guest.getName() + " checked out from " + guestRoom.name);
        lock.unlock();
    }

    public void checkDirtyRooms(Maid maid) {
        lock.lock();
        if (!awaitingCleaning.isEmpty()) {
            var room = awaitingCleaning.poll();
            maid.cleanRoom(room, availableRooms);
        }
        lock.unlock();
    }

}
