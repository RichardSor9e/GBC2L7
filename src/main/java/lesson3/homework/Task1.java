package lesson3.homework;

import java.util.*;
import java.util.function.BiFunction;

public class Task1 {

    private static final String[] INPUT_DATA = {"a", "b", "b", "b", "c", "d", "e", "e", "f", "g", "g", "g", "h", "g", "i", "a"};


    public static void main(String[] args) {

        Map<String, Integer> frequencyByWord = new TreeMap<>();

        for (String word : INPUT_DATA) {
//            Integer frequency = frequencyByWord.get(word);
//            if (frequency == null) {
//                frequency = 0;
//            }

/*            Integer frequency = frequencyByWord.getOrDefault(word, 0);

            frequencyByWord.put(word, ++frequency);*/

/*            frequencyByWord.merge(word, 1, new BiFunction<Integer, Integer, Integer>() {
                @Override
                public Integer apply(Integer integer, Integer integer2) {
                    return integer + integer2;
                }
            });*/


//            frequencyByWord.merge(word, 1, (integer, integer2) -> Integer.sum(integer, integer2));


            frequencyByWord.merge(word, 1, Integer::sum);
        }

        frequencyByWord.forEach((word, frequency) -> System.out.printf("%s : %d%n", word, frequency));
    }
}
