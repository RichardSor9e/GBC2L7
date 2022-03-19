package lesson5;

public class TestSync {
    public static final int STEPS = 100000;
    public static int DELAY = 500;
    public static int counter = 0;

    public static void main(String[] args) {
        TestSync testSync = new TestSync();
        TestSync testSync2 = new TestSync();
        testSync.incCounter();
        testSync.decCounter();

        var t1 = new Thread(testSync::incCounter);
        var t2 = new Thread(() -> testSync2.decCounter());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(TestSync.counter);
    }

    public synchronized void incCounter() {
        for (int i = 0; i < STEPS; i++) {
            counter++;
        }
    }

    public synchronized void decCounter() {
        for (int i = 0; i < STEPS; i++) {
            counter--;
        }
    }
}
