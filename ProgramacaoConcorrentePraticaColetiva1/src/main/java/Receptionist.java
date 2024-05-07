 //O hotel deve contar com vários recepcionistas, que trabalham juntos e que devem alocar hóspedes
 //apenas em quartos vagos

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

    @Override
    public void run() {
        while (!hotel.finished.get()) // AtomicBoolean {
            try {
//                System.out.println(this.getName() + " checking wait queue");
                hotel.checkRoomAndGuest(this);
                Thread.sleep(400);
                hotel.checkCityGuests(this);
                Thread.sleep(400);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

