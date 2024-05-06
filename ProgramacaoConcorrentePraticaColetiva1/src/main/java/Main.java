public class Main {
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
}
