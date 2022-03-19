package lesson1.part1;

public class Main {

    public static void main(String[] args) {
        Cat cat = new Cat();
        Dog dog = new Dog();
        Duck duck = new Duck();
        Plane plane = new Plane();
        Wolf wolf = new Wolf();

        Participant racoon = new Participant() {
            @Override
            public void jump() {

            }

            @Override
            public void run() {

            }
        };

        Animal kangaroo = new Animal() {

            @Override
            public void run() {

            }
        };

        Cat cat2 = new Cat() {
            @Override
            public void jump() {
                super.jump();
            }

            @Override
            public String toString() {
                return super.toString();
            }
        };


        Animal[] animals = {cat, dog, duck, wolf, kangaroo};
        Flying[] flyings = {duck, plane};
        Runner[] runners = {dog, duck, cat, wolf, racoon, kangaroo};

//        runners[1].run();
    }
}
