package lesson1.part2;

public enum Color {
    BLACK("черный"),
    WHITE("белый"),
    RED("рыжий"),
    GRAY("серый");

    private String russianColor;

    Color(String russianColor) {
        this.russianColor = russianColor;
    }

    public String getRussianColor() {
        return russianColor;
    }

    public String getEnglishColor() {
        return name().toLowerCase();
    }

//    abstract void action();
}
