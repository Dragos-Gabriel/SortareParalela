
package sortareparalela;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class CsvExporter {

    private CsvExporter() {
    }

    public static void export(
            List<BenchmarkResult> results,
            String fileName) throws IOException {

        Path path = Path.of(fileName);
        createFolder(path);

        try (BufferedWriter writer =
                Files.newBufferedWriter(path)) {

            writer.write(
                    "Fisier,Elemente,Test,Metoda,Timp_ns,Timp_ms");
            writer.newLine();

            for (BenchmarkResult r : results) {

                writer.write(String.format(
                        Locale.US,
                        "%s,%d,%d,%s,%d,%.3f",
                        r.getInputFile(),
                        r.getNumberOfElements(),
                        r.getTestNumber(),
                        r.getMethod(),
                        r.getTimeNanoseconds(),
                        r.getTimeMilliseconds()));

                writer.newLine();
            }
        }

        System.out.println("CSV salvat: " + path.toAbsolutePath());
    }

    public static void exportAverages(
            List<BenchmarkResult> results,
            String fileName) throws IOException {

        Path path = Path.of(fileName);
        createFolder(path);

        var averages = ConsoleTable.calculateAverages(results);

        try (BufferedWriter writer =
                Files.newBufferedWriter(path)) {

            writer.write("Elemente,Metoda,Timp_mediu_ms");
            writer.newLine();

            for (var entry : averages.entrySet()) {

                for (var method : entry.getValue().entrySet()) {

                    writer.write(String.format(
                            Locale.US,
                            "%d,%s,%.3f",
                            entry.getKey(),
                            method.getKey(),
                            method.getValue()));

                    writer.newLine();
                }
            }
        }

        System.out.println(
                "CSV medii salvat: " + path.toAbsolutePath());
    }

    private static void createFolder(Path path)
            throws IOException {

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
    }
}
