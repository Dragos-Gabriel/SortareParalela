
package sortareparalela;

import java.util.Locale;

public class MemoryAnalyzer {

    private static final long BYTES_PER_INT = Integer.BYTES;

    // Estimare: trei vectori + spatiu auxiliar paralel
    private static final long BYTES_PER_ELEMENT_ESTIMATE = 16L;

    private MemoryAnalyzer() {
    }

    public static long estimatedBytes(int size) {
        return (long) size * BYTES_PER_ELEMENT_ESTIMATE;
    }

    public static long maxHeap() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long availableHeap() {

        Runtime runtime = Runtime.getRuntime();

        long used = runtime.totalMemory()
                - runtime.freeMemory();

        return Math.max(0, runtime.maxMemory() - used);
    }

    public static double toMiB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }

    public static String formatMiB(long bytes) {
        return String.format(
                Locale.US, "%.2f MiB", toMiB(bytes));
    }

    public static void analyze(int size) {

        long oneVector = (long) size * BYTES_PER_INT;
        long threeVectors = oneVector * 3;
        long auxiliary = oneVector;
        long estimated = estimatedBytes(size);

        System.out.println();
        System.out.println("========== ANALIZA MEMORIE ==========");

        System.out.println("Numar elemente: " + size);

        System.out.println(
                "Memorie per element: 4 octeti");

        System.out.println(
                "Un vector: " + oneVector + " octeti ("
                + formatMiB(oneVector) + ")");

        System.out.println(
                "Trei vectori: " + threeVectors
                + " octeti (" + formatMiB(threeVectors) + ")");

        System.out.println(
                "Memorie auxiliara estimata: "
                + formatMiB(auxiliary));

        System.out.println(
                "Total estimat: " + estimated
                + " octeti (" + formatMiB(estimated) + ")");

        System.out.println(
                "Heap maxim JVM: " + formatMiB(maxHeap()));

        System.out.println(
                "Heap disponibil estimat: "
                + formatMiB(availableHeap()));

        if (canRun(size)) {
            System.out.println(
                    "Evaluare: memoria pare suficienta.");
        } else {
            System.out.println(
                    "AVERTISMENT: memorie insuficienta "
                    + "sau apropiata de limita configurata.");
        }

        System.out.println(
                "Nota: valorile sunt estimative, "
                + "nu reprezinta un test de alocare real.");

        System.out.println("=====================================");
    }

    public static boolean canRun(int size) {

        long required = estimatedBytes(size);
        long available = availableHeap();

        // Rezerva de siguranta de 25%
        return required <= (long) (available * 0.75);
    }

    public static void analyzeThreshold() {

        long available = availableHeap();

        long theoretical =
                available / BYTES_PER_ELEMENT_ESTIMATE;

        long recommended = (long) (theoretical * 0.75);

        System.out.println();
        System.out.println("===== PRAGURI ESTIMATE DE MEMORIE =====");

        System.out.println(
                "Heap JVM disponibil estimat: "
                + formatMiB(available));

        System.out.printf(
                "Prag teoretic aproximativ: %,d elemente%n",
                theoretical);

        System.out.printf(
                "Prag cu rezerva de siguranta: %,d elemente%n",
                recommended);

        System.out.println(
                "Aceste praguri nu garanteaza reusita.");
        System.out.println(
                "Memoria JVM include si alte obiecte, "
                + "iar vectorii necesita alocari continue.");

        System.out.println("======================================");
    }
}

