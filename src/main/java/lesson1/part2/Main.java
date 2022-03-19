package lesson1.part2;

public class Main {
    public static void main(String[] args) {
//        testEnum();
//        testAnonClasses();
        testInnerCalsses();
    }

    private static void testEnum() {
        Cat cat1 = new Cat();

        cat1.setColor(Color.RED);

/*        switch (cat1.getColor()) {
            case RED ->
        }*/

        System.out.println(cat1);
    }

    private static void testAnonClasses() {
/*        AbstactClass foo = new Foo();
        AbstactClass bar = new Bar();*/

        SomeOneFileToImplement foo = () -> System.out.println("Привет, вселенная!");

        SomeOneFileToImplement bar = new SomeOneFileToImplement() {
            @Override
            public void action() {
                System.out.println("Привет, Гик Брейнс");
            }
        };

        foo.action();
        bar.action();
    }

    private static void testInnerCalsses() {
        Cat cat = new Cat(Color.BLACK, "Мартин", 3, 5);
//        Cat.CatAttributes catAttributes = cat.new CatAttributes("Мартин", 3, 5);
//        System.out.println(catAttributes);
        System.out.println(cat);
    }
}
