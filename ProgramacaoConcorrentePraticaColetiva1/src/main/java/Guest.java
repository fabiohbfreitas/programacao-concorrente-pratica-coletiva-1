import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Guest extends Thread {
    Hotel hotel;
    Room currentRoom;
    Lock lock;
    String familyID;

    public Guest(String name, Hotel hotel) {
        super(name);
        this.hotel = hotel;
        lock = new ReentrantLock(true);
    }

    public Guest(String name, Hotel hotel, String familyID) {
        super(name);
        this.hotel = hotel;
        this.familyID = familyID;
        lock = new ReentrantLock(true);
    }

    @Override
    public void run() {
        hotel.arrive(this);
        System.out.println(this.getName() + " arrived.");
        int waitTimes = 0;
        int n = 0;

        while (true) {
            try {
                if (hotel.finished.get()) {
                    break;
                }
                if (currentRoom != null) {
                    if (n > 1) {
                        hotel.checkout(this);
                        break;
                    }
                    Thread.sleep(1500 + ThreadLocalRandom.current().nextInt(500));
                    System.out.println(this.getName() + " is inside " + currentRoom.name);
                    // TODO: FAMILIA TODA DEVE SAIR
                    Thread.sleep(1500 + ThreadLocalRandom.current().nextInt(1000));
                    if (/*!this.currentRoom.guests.isEmpty()&& */ n < 1){
                        goesForWalk(this);
                    }
                    n += 1;
                    waitTimes = 0;
                    continue;
                }

                if (waitTimes >= 20) {
                    hotel.leave(this);
                    break;
                }
                if (hotel.isWaiting(this)) {
                    waitTimes++;
                    System.out.println(this.getName() + " waiting...");

                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }


    }

 public void goesForWalk(Guest guest) {
        try {
            lock.lock();
            Thread.sleep(500 + ThreadLocalRandom.current().nextInt(5000));

            if (familyID != null) {
//                hotel.addFamilyToCityGuests(familyID);
            } else {
                hotel.awaitingInCityGuests.add(guest);
                hotel.receptionStoreKeys(guest.currentRoom, guest);
                System.out.println(guest.getName() + " goes for a walk in the city...");
            }

            Thread.sleep(2000);
            lock.unlock();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
