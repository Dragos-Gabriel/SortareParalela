package sortareparalela;

import java.util.List;

public class ConsoleTable {

    private ConsoleTable() {
        // Clasă utilitară
    }

    public static void print(List<BenchmarkResult> results) {

        if (results == null || results.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        System.out.println();
        System.out.println(
                "====================================================================================================");

        System.out.printf(
                "| %-12s | %-6s | %-25s | %-18s | %-15s |%n",
                "Elemente",
                "Test",
                "Metoda",
                "Timp (ns)",
                "Timp (ms)");

        System.out.println(
                "====================================================================================================");

        for (BenchmarkResult result : results) {

            System.out.printf(
                    "| %-12d | %-6d | %-25s | %-18d | %-15.3f |%n",
                    result.getNumberOfElements(),
                    result.getTestNumber(),
                    result.getMethod(),
                    result.getTimeNanoseconds(),
                    result.getTimeMilliseconds());
        }

        System.out.println(
                "====================================================================================================");
    }

    public static void printAverageResults(
            List<BenchmarkResult> results) {

        System.out.println();
        System.out.println(
                "================ REZULTATE MEDII ================");

        System.out.printf(
                "| %-12s | %-25s | %-15s |%n",
                "Elemente",
                "Metoda",
                "Timp mediu (ms)");

        System.out.println(
                "===================================================");

        java.util.Map<String, java.util.Map<Integer, Double>>
                averages = new java.util.HashMap<>();

        for (BenchmarkResult result : results) {

            averages
                    .computeIfAbsent(
                            result.getMethod(),
                            key -> new java.util.HashMap<>())
                    .merge(
                            result.getNumberOfElements(),
                            result.getTimeMilliseconds(),
                            Double::sum);
        }

        java.util.Map<String, java.util.Map<Integer, Integer>>
                counts = new java.util.HashMap<>();

        for (BenchmarkResult result : results) {

            counts
                    .computeIfAbsent(
                            result.getMethod(),
                            key -> new java.util.HashMap<>())
                    .merge(
                            result.getNumberOfElements(),
                            1,
                            Integer::sum);
        }

        for (String method : averages.keySet()) {

            for (Integer elements :
                    averages.get(method).keySet()) {

                double total =
                        averages.get(method).get(elements);

                int count =
                        counts.get(method).get(elements);

                double average = total / count;

                System.out.printf(
                        "| %-12d | %-25s | %-15.3f |%n",
                        elements,
                        method,
                        average);
            }
        }

        System.out.println(
                "===================================================");
    }
}
