package sortareparalela;

import java.util.Arrays;

public class SortingService {

    private SortingService() {
        // Clasă utilitară
    }

    public static void sequentialSort(int[] array) {
        Arrays.sort(array);
    }

    public static void parallelSort(int[] array) {
        Arrays.parallelSort(array);
    }
}
