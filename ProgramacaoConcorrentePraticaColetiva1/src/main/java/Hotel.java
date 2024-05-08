import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    Queue<Guest> awaitingCityGuests;
    Multimap<String, Room> familyRooms;
    public AtomicBoolean finished = new AtomicBoolean(false);

    public Hotel() {
        lock = new ReentrantLock(true);
        waitQueue = new LinkedList<>();
        complaints = 0;
        availableRooms = createRooms();
        occupiedRooms = new ArrayList<>();
        awaitingCleaning = new LinkedList<>();
        awaitingCityGuests = new LinkedList<>();
        familyRooms = new Multimap<>();

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
        for (int n = 0; n < 3; n++) {
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
        if (guest.familyID != null) {
            List<Guest> temp = new LinkedList<>();
            while (!waitQueue.isEmpty()) {
                var tempGuest = waitQueue.poll();
                if (tempGuest.familyID != null && tempGuest.familyID.equals(guest.familyID)) {
                    complaints += 1;
                } else {
                    temp.add(tempGuest);
                }
            }
            for (var tempGuest : temp) {
                waitQueue.add(tempGuest);
            }
            System.out.println("All family " + guest.familyID + " has left.");
        }

        lock.unlock();
    }

    private Queue<Room> createRooms() {
        var rooms = new LinkedList<Room>();
        for (int n = 0; n < 10; n++) {
            rooms.add(new Room("Room " + n));
        }
        return rooms;
    }

    public void checkRoomAndGuest(Receptionist receptionist) {
        lock.lock();
        if (!waitQueue.isEmpty() && !availableRooms.isEmpty()) {
            var guest = waitQueue.poll(); // returns and removes the first
            if (guest.familyID == null) {
                var room = availableRooms.poll();
                receptionist.giveKeys(room,guest);
                System.out.println(receptionist.getName() + " gave the room " + room.name + " to " + guest.getName());
            } else {
                if (availableRooms.size() < 2) {
                    waitQueue.add(guest);
                    return;
                }
                List<Room> rooms = getRoomsForFamily(guest.familyID);
                List<Guest> family = new ArrayList<>();
                family.add(guest);

                List<Guest> temp = new ArrayList<>();
                while (waitQueue.size() > 0) {
                    var next = waitQueue.poll();
                    if (next.familyID.equals(guest.familyID)) {
                        family.add(next);
                    } else {
                        temp.add(next);
                    }
                }
                for (var tempGuest : temp) {
                    waitQueue.add(tempGuest);
                }
//                System.out.println("Family: " + family.size());
//                for (var f: family) {
//                    System.out.println("  " + f.getName());
//                }
                receptionist.giveKeysToFamily(rooms, family);
                System.out.println("Family " + guest.familyID + " are in rooms: " + rooms.stream().map(r -> r.name).toList());
            }
        }

        lock.unlock();
    }

    public List<Room> getRoomsForFamily(String id) {
        List<Room> rooms = new ArrayList<>();
        var familySize = waitQueue.stream().filter(e -> e.familyID.equalsIgnoreCase(id)).count();
        var firstRoom = availableRooms.poll();
        familyRooms.put(id, firstRoom);
        rooms.add(firstRoom);
        if (familySize <= Room.MAX_GUESTS) {
            var secondRoom = availableRooms.poll();
            rooms.add(secondRoom);
            familyRooms.put(id, secondRoom);
        }
        return rooms;
    }

    public void assignRoomKeysToCityGuests(Receptionist receptionist) {
    lock.lock();
    try {
        if (!awaitingCityGuests.isEmpty()) {
            var guest = awaitingCityGuests.element();
            if (!awaitingCleaning.contains(guest.currentRoom)) {
                receptionist.giveKeys(guest.currentRoom, guest);
                System.out.println(receptionist.getName() + " returned the keys to " + guest.getName() + " for room " + guest.currentRoom.name);
                awaitingCityGuests.remove(guest);
            }
        }
    } finally {
        lock.unlock();
    }
    }
    public void addFamilyToCityGuests(String familyID) {
    for (Room room : occupiedRooms) {
        for (Guest guest : room.guests) {
            if (guest.familyID != null && guest.familyID.equals(familyID)) {
                System.out.println(guest.getName() + " goes for a walk in the city...");
                awaitingCityGuests.add(guest);
            }
        }
    }
}

    public void receptionStoreKeys(Room room, Guest guest) {
        lock.lock();
        awaitingCleaning.add(guest.currentRoom);
        room.guests.remove(guest);
        lock.unlock();
    }

    public void checkout(Guest guest) {
        lock.lock();
        if (guest.familyID == null) {
            var guestRoom = guest.currentRoom;
            guestRoom.guests.remove(guest);
            occupiedRooms.remove(guestRoom);
            awaitingCleaning.add(guestRoom);
            guest.currentRoom = null;
            System.out.println(guest.getName() + " checked out from " + guestRoom.name);
        } else {

            var rooms = familyRooms.get(guest.familyID);
            guest.currentRoom = null;
            for (var room : rooms) {
                for (var familyGuest: room.guests) {
                    familyGuest.currentRoom = null;

                    System.out.println(familyGuest.getName() + " ("+ familyGuest.familyID+") " + " checked out from " + room.name);
                }
                occupiedRooms.remove(room);
                awaitingCleaning.add(room);
            }
            familyRooms.remove(guest.familyID);
        }
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
