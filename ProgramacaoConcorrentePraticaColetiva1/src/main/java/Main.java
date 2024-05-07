import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class Main {
	private static final int MIN_FAMILY_SIZE = 2;
	private static final int MAX_FAMILY_SIZE = 6; // Change this to whatever maximum size you want

	public static void main(String[] args) {
		var hotel = new Hotel();


		for (var i = 0; i < 10; i++) {
			var newfamily = createFamily(i, hotel);
			newfamily.run();
		}

		try {
			Thread.sleep(35000);
			System.out.println("================================");
			System.out.println("Waiting: ");
			for (var a : hotel.waitQueue) {
				System.out.println("  "+ a.getName());
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
	public static int randomFamilySize(){
		Random random = new Random();
		int randomNum;
		randomNum = random.nextInt(MAX_FAMILY_SIZE + 1) + MIN_FAMILY_SIZE; //  Random integer in range of MIN_FAMILY_SIZE -> MAX_FAMILY_SIZE

		return randomNum;
	}
	
	//MARK: Returns a family of guests 
	public static Family createFamily(int familyNum, Hotel hotel) {
		int familySize = randomFamilySize();
		Family newFamily = new Family(familyNum, hotel);
		
		for (var i = 0; i < familySize; i++) {
			var newGuest = new Guest("Guest " + i + " from family " + familyNum, hotel); // Creates a new guest
			System.out.println("Guest " + i + " from family " + familyNum + " arrived;");
			newFamily.members.add(newGuest); // appends new guest to it's family
			
		}
		
		return newFamily;
	}
}
