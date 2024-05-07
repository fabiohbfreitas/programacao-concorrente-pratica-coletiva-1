import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Guest extends Thread {
    Hotel hotel;
    Room currentRoom;
    Lock lock;

    public Guest(String name, Hotel hotel) {
        super(name);
        this.hotel = hotel;
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
                    n += 1;
                    Thread.sleep(1500 + ThreadLocalRandom.current().nextInt(1000));
                    if (!this.currentRoom.guests.isEmpty()){
                        goesForWalk(this);
                    }
                    continue;
                }

                if (waitTimes >= 10) {
                    hotel.leave(this);
                    break;
                }
                if (hotel.isWaiting(this)) {
                    waitTimes++;
                    System.out.println(this.getName() + " Goes fo a walk and will try again later...");
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
            hotel.awaitingCityGuests.add(guest);
            System.out.println(guest.getName() + "Goes for a walk in the city...");

            hotel.receptionStoreKeys(guest.currentRoom, guest);
            Thread.sleep(2000);
            lock.unlock();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }




}
