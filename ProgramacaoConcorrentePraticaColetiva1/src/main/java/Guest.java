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
        int n = 0;
        while(true) {
            try {
                if (currentRoom != null) {
                    if (n > 1) {
                        hotel.checkout(this);
                        break;
                    }
                    Thread.sleep(1500 + ThreadLocalRandom.current().nextInt(500));
                    System.out.println(this.getName() + " is inside " + currentRoom.name);
                    n+=1;
                    continue;
                }
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
}
