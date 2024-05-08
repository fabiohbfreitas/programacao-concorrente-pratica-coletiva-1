 //O hotel deve contar com vários recepcionistas, que trabalham juntos e que devem alocar hóspedes
 //apenas em quartos vagos

 import java.util.List;
 import java.util.concurrent.ThreadLocalRandom;

 public class Receptionist extends Thread {
    Hotel hotel;

    public Receptionist(String name, Hotel hotel) {
        super(name);
        this.hotel = hotel;
    }

    public void giveKeys(Room room, Guest guest) {
        room.guests.add(guest);  // add guest to a room
        guest.currentRoom = room; //  att quest's current room
        if (!hotel.occupiedRooms.contains(room)) {
            hotel.occupiedRooms.add(room); // add the room to the occupied
        }
    }

     public void giveKeysToFamily(List<Room> rooms, List<Guest> guest) {
         var firstRoom = rooms.get(0);
         var secondRoom = rooms.get(1);
         for (var currentGuest: guest) {
             if (firstRoom.guests.size() < Room.MAX_GUESTS) {
                 firstRoom.guests.add(currentGuest);
                 currentGuest.currentRoom = firstRoom;
             } else {
                 secondRoom.guests.add(currentGuest);
                 currentGuest.currentRoom = secondRoom;
             }
         }
     }

    @Override
    public void run() {
        while (!hotel.finished.get()) // AtomicBoolean {
            try {
//                System.out.println(this.getName() + " checking wait queue");
                Thread.sleep(1800 + ThreadLocalRandom.current().nextInt(500));
                hotel.checkRoomAndGuest(this);
                // TODO: FAMILIA TODA DEVE VOLTAR
//                hotel.assignRoomKeysToCityGuests(this);
//                Thread.sleep(400);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

