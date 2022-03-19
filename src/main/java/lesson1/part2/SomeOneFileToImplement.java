package lesson1.part2;

public interface SomeOneFileToImplement {
     void action();

     default boolean isTrue() {
          return true;
     }
}
