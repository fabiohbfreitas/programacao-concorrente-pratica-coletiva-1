import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hotel {
    Lock lock;
    Queue<Guest> waitQueue;
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

    public void arrive(Guest guest) {
        lock.lock();
        waitQueue.add(guest);
        lock.unlock();
    }

    public boolean isWaiting(Guest guest) {
        lock.lock();
        if (waitQueue.contains(guest)) {
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    public void leave(Guest guest) {
        lock.lock();
        waitQueue.remove(guest);
        complaints += 1;
        System.out.println(guest.getName() + " left a complaint.");
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
            var guest = waitQueue.poll(); // returns and removes the first
            var room = availableRooms.poll();
            receptionist.giveKeys(room,guest);
            System.out.println(receptionist.getName() + " gave the room " + room.name + " to " + guest.getName());
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

    public void returnToRoom(Guest guest) {
        lock.lock();
        try {
            while (awaitingCleaning.contains(guest.currentRoom)) {
                System.out.println(guest.getName() + " returns to their room, but it's still awaiting cleaning. Waiting...");
                try {
                    Thread.sleep(2000); // Wait for 2 seconds before trying again
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt(); // Re-interrupt the thread
                }
            }
            guest.currentRoom.guests.add(guest);
            System.out.println(guest.getName() + " returns to their room.");
        } finally {
            lock.unlock();
        }
    }

    public void goesForWalk(Guest guest)  {
        try {
            awaitingCleaning.add(guest.currentRoom);
            System.out.println(guest.getName() + "Goes for a walk in the city...");
            guest.currentRoom.guests.remove(guest);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
