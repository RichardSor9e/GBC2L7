package lesson2;

import java.io.IOException;

public class CatFoundException extends IOException {
    public CatFoundException() {
        super("КОТ НЕ ПРОЙДЕТ");
    }

    public CatFoundException(int i, String name) {
        super(String.format("%s прокрался на вечеринку под номером %d", name, i));
    }
}
