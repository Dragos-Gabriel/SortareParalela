
package sortareparalela;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenchmarkService {

    public List<BenchmarkResult> runBenchmark(
            String fileName,
            int numberOfTests) throws IOException {

        if (numberOfTests <= 0) {
            throw new IllegalArgumentException(
                    "Numarul de teste trebuie sa fie pozitiv.");
        }

        int size = InputFileReader.readSize(fileName);

        MemoryAnalyzer.analyze(size);

        if (!MemoryAnalyzer.canRun(size)) {
            throw new IOException(
                    "Testul a fost oprit preventiv "
                    + "din cauza estimarii de memorie.");
        }

        int[] original = InputFileReader.readVector(fileName);

        List<BenchmarkResult> results = new ArrayList<>();

        System.out.println();
        System.out.println("========== BENCHMARK ==========");
        System.out.println("Fisier: " + fileName);
        System.out.println("Elemente: " + size);
        System.out.println("Teste: " + numberOfTests);

        for (int test = 1; test <= numberOfTests; test++) {

            System.out.println();
            System.out.println(
                    "Testul " + test + "/" + numberOfTests);

            // Copii identice ale vectorului citit din fisier
            int[] sequentialArray =
                    Arrays.copyOf(original, original.length);

            int[] parallelArray =
                    Arrays.copyOf(original, original.length);

            long sequentialTime;
            long parallelTime;

            // Alternam ordinea pentru a reduce
            // influenta ordinii de executie.
            if (test % 2 == 1) {

                long start = System.nanoTime();
                SortingService.sequentialSort(sequentialArray);
                sequentialTime = System.nanoTime() - start;

                start = System.nanoTime();
                SortingService.parallelSort(parallelArray);
                parallelTime = System.nanoTime() - start;

            } else {

                long start = System.nanoTime();
                SortingService.parallelSort(parallelArray);
                parallelTime = System.nanoTime() - start;

                start = System.nanoTime();
                SortingService.sequentialSort(sequentialArray);
                sequentialTime = System.nanoTime() - start;
            }

            // Verificarea corectitudinii
            if (!isSorted(sequentialArray)) {
                throw new IllegalStateException(
                        "Sortarea secventiala este incorecta.");
            }

            if (!isSorted(parallelArray)) {
                throw new IllegalStateException(
                        "Sortarea paralela este incorecta.");
            }

            if (!Arrays.equals(
                    sequentialArray, parallelArray)) {

                throw new IllegalStateException(
                        "Rezultatele sortarilor sunt diferite.");
            }

            BenchmarkResult sequentialResult =
                    new BenchmarkResult(
                            fileName, size, test,
                            "Arrays.sort()", sequentialTime);

            BenchmarkResult parallelResult =
                    new BenchmarkResult(
                            fileName, size, test,
                            "Arrays.parallelSort()", parallelTime);

            results.add(sequentialResult);
            results.add(parallelResult);

            System.out.printf(
                    "Secvential: %.3f ms%n",
                    sequentialResult.getTimeMilliseconds());

            System.out.printf(
                    "Paralel:    %.3f ms%n",
                    parallelResult.getTimeMilliseconds());

            System.out.println(
                    "Verificare: vectori sortati corect "
                    + "si rezultate identice.");
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

