package sortareparalela;

import java.util.Random;

public class DataGenerator {

    private static final Random RANDOM = new Random();

    private DataGenerator() {
        // Clasă utilitară
    }

    public static int[] generate(int size) {

        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(1_000_000);
        }

        return array;
    }
}
