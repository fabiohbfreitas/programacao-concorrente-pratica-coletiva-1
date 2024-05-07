import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Family extends Thread {
	int familyID;
	Hotel hotel;
	Room currentRoom;
    List<Guest> members;
    
    public Family(int familyID, Hotel hotel) {
		super();
		this.familyID = familyID;
		this.members = new ArrayList<>();
		this.hotel = hotel;
	}

	@Override
    public void run() {
        hotel.arrive(this);
        System.out.println("Family " + this.familyID + " arrived!");
        int waitTimes = 0;
        int n = 0;
        while(true) {
            try {
                if (hotel.finished.get()) {
                    break;
                }
                if (currentRoom != null) {
                	for(var  i = 0; i < this.members.size(); i++ ) {
                		this.members.get(i).run();
                	}
                }

                if (waitTimes >= 10) {
                    hotel.leave(this);
                    break;
                }
                if (hotel.isWaiting(this)) {
                    waitTimes++;
                    System.out.println("Family " + this.familyID + " is waiting a room...");
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                }
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
}