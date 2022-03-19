package lesson1.part2;

public class Cat {

    private Color color;
    private CatAttributes catAttributes;

    public enum Color2 {
        BLACK, RED, WHITE

    }

    public Color getColor() {
        return color;
    }

    public Cat(Color color, String name, int age, int weight) {
        this.color = color;
        catAttributes = new CatAttributes(name, age, weight);
    }

    public Cat() {
        this(Color.RED, "", 0, 0);
    }

    interface test {
        void test();
    }

    private class CatAttributes implements test {
        private String name;
        private int age;
        private int weight;

        public CatAttributes(String name, int age, int weight) {
            this.name = name;
            this.age = age;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "CatAttributes{" + "name='" + name + '\'' + ", age=" + age + ", weight=" + weight + '}';
        }

        @Override
        public void test() {

        }
    }

    class Test1 {

    }

    static class Test2 {

    }

    public void setColor(Color color) {
        class Name {

        }
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cat{" + "color='" + color.getEnglishColor() + '\'' + " " + catAttributes;
    }
}
