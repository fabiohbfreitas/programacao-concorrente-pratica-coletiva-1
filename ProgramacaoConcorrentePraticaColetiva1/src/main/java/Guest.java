import java.util.concurrent.ThreadLocalRandom;

public class Guest extends Thread {
    Hotel hotel;
    Room currentRoom;

    public Guest(String name, Hotel hotel) {
        super(name);
        this.hotel = hotel;
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
                    goesForWalk(this);
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

 public void goesForWalk(Guest guest)  {
        try {
            hotel.awaitingCityGuests.add(guest);
            System.out.println(guest.getName() + "Goes for a walk in the city...");

           hotel.receptionStoreKeys(guest.currentRoom, guest);
            Thread.sleep(2000);


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }




}
