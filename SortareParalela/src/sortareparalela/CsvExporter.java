package sortareparalela;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    private CsvExporter() {
        // Clasă utilitară
    }

    public static void export(
            List<BenchmarkResult> results,
            String fileName) {

        File file = new File(fileName);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            // Header CSV
            writer.write(
                    "Numar elemente,Test,Metoda,Timp (ns),Timp (ms),Timp (s)");

            writer.newLine();

            for (BenchmarkResult result : results) {

                writer.write(
                        result.getNumberOfElements()
                        + ","
                        + result.getTestNumber()
                        + ","
                        + result.getMethod()
                        + ","
                        + result.getTimeNanoseconds()
                        + ","
                        + String.format(
                                java.util.Locale.US,
                                "%.3f",
                                result.getTimeMilliseconds())
                        + ","
                        + String.format(
                                java.util.Locale.US,
                                "%.6f",
                                result.getTimeSeconds()));

                writer.newLine();
            }

            System.out.println();
            System.out.println(
                    "CSV salvat cu succes: "
                    + file.getAbsolutePath());

        } catch (IOException e) {

            System.err.println(
                    "Eroare la salvarea fisierului CSV.");

            e.printStackTrace();
        }
    }
}
