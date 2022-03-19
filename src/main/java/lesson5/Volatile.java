package lesson5;

public class Volatile {
    private volatile static int myInt = 0;

    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int localVar = myInt;
            while (myInt < 5) {
                if (localVar != myInt) {
                    localVar = myInt;
                    System.out.println(localVar + " t1");
                }
            }
        }
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {

            int localVar = myInt;
            while (myInt < 5) {
                myInt = ++localVar;
                System.out.println(localVar + " t2");
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
