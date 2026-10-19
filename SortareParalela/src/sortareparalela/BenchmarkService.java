package sortareparalela;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenchmarkService {

    public List<BenchmarkResult> runBenchmark(
            int numberOfElements,
            int numberOfTests) {

        List<BenchmarkResult> results = new ArrayList<>();

        System.out.println();
        System.out.println("==============================================");
        System.out.println("Testare pentru " + numberOfElements + " elemente");
        System.out.println("Numar de teste: " + numberOfTests);
        System.out.println("==============================================");

        for (int test = 1; test <= numberOfTests; test++) {

            System.out.println();
            System.out.println("Testul " + test + " din " + numberOfTests);

            // Generam vectorul initial
            int[] original = DataGenerator.generate(numberOfElements);

            // Cream doua copii identice
            int[] sequentialArray = Arrays.copyOf(
                    original,
                    original.length);

            int[] parallelArray = Arrays.copyOf(
                    original,
                    original.length);

            // =========================================
            // SORTARE SECVENȚIALĂ
            // =========================================

            long startSequential = System.nanoTime();

            SortingService.sequentialSort(sequentialArray);

            long endSequential = System.nanoTime();

            long sequentialTime =
                    endSequential - startSequential;

            // Verificam sortarea
            if (!isSorted(sequentialArray)) {
                throw new IllegalStateException(
                        "Eroare: Arrays.sort() nu a sortat corect vectorul.");
            }

            BenchmarkResult sequentialResult =
                    new BenchmarkResult(
                            numberOfElements,
                            test,
                            "Arrays.sort()",
                            sequentialTime);

            results.add(sequentialResult);

            System.out.printf(
                    "Arrays.sort()       : %.3f ms%n",
                    sequentialResult.getTimeMilliseconds());

            // =========================================
            // SORTARE PARALELĂ
            // =========================================

            long startParallel = System.nanoTime();

            SortingService.parallelSort(parallelArray);

            long endParallel = System.nanoTime();

            long parallelTime =
                    endParallel - startParallel;

            // Verificam sortarea
            if (!isSorted(parallelArray)) {
                throw new IllegalStateException(
                        "Eroare: Arrays.parallelSort() nu a sortat corect vectorul.");
            }

            BenchmarkResult parallelResult =
                    new BenchmarkResult(
                            numberOfElements,
                            test,
                            "Arrays.parallelSort()",
                            parallelTime);

            results.add(parallelResult);

            System.out.printf(
                    "Arrays.parallelSort(): %.3f ms%n",
                    parallelResult.getTimeMilliseconds());

            // Verificam ca rezultatele celor doua metode sunt identice
            if (!Arrays.equals(
                    sequentialArray,
                    parallelArray)) {

                throw new IllegalStateException(
                        "Eroare: rezultatele celor doua metode sunt diferite.");
            }

            System.out.println(
                    "Verificare: rezultatele sortarii sunt identice.");
        }

        return results;
    }

    private boolean isSorted(int[] array) {

        for (int i = 1; i < array.length; i++) {

            if (array[i - 1] > array[i]) {
                return false;
            }
        }

        return true;
    }
}
