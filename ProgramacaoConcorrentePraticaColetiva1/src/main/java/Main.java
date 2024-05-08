import java.util.Random;

public class Main {
//	private static final int MIN_FAMILY_SIZE = 2;
//	private static final int MAX_FAMILY_SIZE = 6;
			
    public static void main(String[] args) {
        var hotel = new Hotel();
        for (var i = 0; i < 1; i++) {
            var newGuest = new Guest("Guest " + i, hotel);
            newGuest.start();
        }
        var g10 = new Guest("Guest 10", hotel, "A");
        g10.start();
        var g11 = new Guest("Guest 11", hotel,"A");
        g11.start();
        var g12 = new Guest("Guest 12", hotel,"A");
        g12.start();
        var g13 = new Guest("Guest 13", hotel,"A");
        g13.start();
        var g14 = new Guest("Guest 14", hotel,"A");
        g14.start();

        try {
            Thread.sleep(30000);
            System.out.println("================================");
            System.out.println("Waiting: ");
            for (var a : hotel.waitQueue) {
                System.out.println("  "+a.getName());
            }

            System.out.println("In Rooms:");
            for (var a: hotel.occupiedRooms) {
                System.out.println("  " + a.guests.get(0).getName());
            }

            System.out.println("Available Rooms:");
            for (var a: hotel.availableRooms) {
                System.out.println("  " + a.name);
            }

            System.out.println("Dirty Rooms:");
            for (var a: hotel.awaitingCleaning) {
                System.out.println("  " + a.name);
            }

            System.out.println("Complaints: " + hotel.complaints);

            System.out.println("finishing...");
            hotel.finished.set(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
  //MARK: Returns a random family size
//  	public static int randomFamilySize(){
//  		Random random = new Random();
//  		int randomNum;
//  		randomNum = random.nextInt(MAX_FAMILY_SIZE + 1) + MIN_FAMILY_SIZE; //  Random integer in range of MIN_FAMILY_SIZE -> MAX_FAMILY_SIZE
//
//  		return randomNum;
//  	}
  	
	//MARK: Create new guests and assign them a family ID 
//	public static  void createFamily(int familyNum, Hotel hotel) {
//		int familySize = randomFamilySize();
//
//		for (var i = 0; i < familySize; i++) {
//			var newGuest = new Guest("Guest " + i + " from family " + familyNum, hotel, familyNum); // Creates a new guest
//			newGuest.start();
//		}
//
//	}
}
