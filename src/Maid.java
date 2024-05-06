
class Maid implements Runnable {
    private final int id;

    public Maid(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            cleanRoom();
        }
    }

    private void cleanRoom() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
