
package sortareparalela;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsoleTable {

    private ConsoleTable() {
    }

    public static void print(List<BenchmarkResult> results) {

        if (results.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        System.out.println();
        System.out.printf(
                "%-12s %-7s %-23s %15s%n",
                "Elemente", "Test", "Metoda", "Timp (ms)");

        System.out.println("-".repeat(64));

        for (BenchmarkResult result : results) {

            System.out.printf(
                    "%-12d %-7d %-23s %15.3f%n",
                    result.getNumberOfElements(),
                    result.getTestNumber(),
                    result.getMethod(),
                    result.getTimeMilliseconds());
        }
    }

    public static Map<Integer, Map<String, Double>>
            calculateAverages(List<BenchmarkResult> results) {

        Map<Integer, Map<String, Double>> totals =
                new TreeMap<>();

        Map<Integer, Map<String, Integer>> counts =
                new TreeMap<>();

        for (BenchmarkResult result : results) {

            int size = result.getNumberOfElements();
            String method = result.getMethod();

            totals.computeIfAbsent(
                    size, k -> new TreeMap<>());

            counts.computeIfAbsent(
                    size, k -> new TreeMap<>());

            totals.get(size).merge(
                    method, result.getTimeMilliseconds(),
                    Double::sum);

            counts.get(size).merge(
                    method, 1, Integer::sum);
        }

        Map<Integer, Map<String, Double>> averages =
                new TreeMap<>();

        for (int size : totals.keySet()) {

            Map<String, Double> methodAverages =
                    new TreeMap<>();

            for (String method : totals.get(size).keySet()) {

                double sum = totals.get(size).get(method);
                int count = counts.get(size).get(method);

                methodAverages.put(method, sum / count);
            }

            averages.put(size, methodAverages);
        }

        return averages;
    }

    public static void printAverageResults(
            List<BenchmarkResult> results) {

        if (results.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        var averages = calculateAverages(results);

        System.out.println();
        System.out.println("========== TIMPI MEDII ==========");

        System.out.printf(
                "%-12s %-23s %15s%n",
                "Elemente", "Metoda", "Medie (ms)");

        System.out.println("-".repeat(54));

        for (var entry : averages.entrySet()) {

            for (var method : entry.getValue().entrySet()) {

                System.out.printf(
                        "%-12d %-23s %15.3f%n",
                        entry.getKey(),
                        method.getKey(),
                        method.getValue());
            }
        }
    }
}
