import java.util.Random;


public class Main {
	private static final int MIN_FAMILY_SIZE = 1;
	private static final int MAX_FAMILY_SIZE = 6; // Change this to whatever maximum size you want

	public static void main(String[] args) {
		var hotel = new Hotel();


		for (var i = 0; i < 15; i++) {
			var newGuest = new Guest("Guest " + i, hotel);
			newGuest.start();
		}

		try {
			Thread.sleep(35000);
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
	public int randomFamilySize(){
		Random random = new Random();
		int randomNum;
		randomNum = random.nextInt(MAX_FAMILY_SIZE + 1);

		return randomNum;
	}
}
