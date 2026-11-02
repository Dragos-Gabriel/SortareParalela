
package sortareparalela;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final BenchmarkService benchmarkService =
            new BenchmarkService();

    private static final List<BenchmarkResult> allResults =
            new ArrayList<>();

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("======================================");
        System.out.println("     SORTARE IN PARALEL - JAVA 17");
        System.out.println("======================================");

        System.out.println(
                "Procesoare disponibile: "
                + Runtime.getRuntime().availableProcessors());

        while (running) {

            printMenu();

            int option = readInt("Alege optiunea: ");

            try {

                switch (option) {

                    case 1 -> testSingleFile();

                    case 2 -> testAllFiles();

                    case 3 -> {
                        ConsoleTable.print(allResults);
                        ConsoleTable.printAverageResults(allResults);
                    }

                    case 4 -> exportResults();

                    case 5 -> generateChart();

                    case 6 ->
                            ConsoleTable.printAverageResults(allResults);

                    case 7 -> InputFileGenerator.generateAll();

                    case 8 -> analyzeMemory();

                    case 0 -> {
                        running = false;
                        System.out.println("Aplicatia s-a inchis.");
                    }

                    default -> System.out.println(
                            "Optiune invalida.");
                }

            } catch (IOException e) {

                System.out.println(
                        "Eroare: " + e.getMessage());

            } catch (OutOfMemoryError e) {

                System.out.println(
                        "MEMORIE INSUFICIENTA!");
                System.out.println(
                        "Incerca un vector mai mic sau "
                        + "mareste memoria JVM (-Xmx).");

                // O eroare de memorie poate lasa JVM
                // intr-o stare instabila.
                running = false;
            }
        }

        scanner.close();
    }

    private static void printMenu() {

        System.out.println();
        System.out.println("============= MENIU =============");

        System.out.println(
                "1. Testeaza un fisier de input");

        System.out.println(
                "2. Testeaza toate fisierele standard");

        System.out.println(
                "3. Afiseaza rezultate si medii");

        System.out.println(
                "4. Salveaza CSV si timpi medii");

        System.out.println(
                "5. Genereaza grafic SVG");

        System.out.println(
                "6. Afiseaza doar timpii medii");

        System.out.println(
                "7. Genereaza fisiere de input");

        System.out.println(
                "8. Analizeaza memoria calculatorului");

        System.out.println(
                "0. Iesire");

        System.out.println("=================================");
    }

    private static void testSingleFile()
            throws IOException {

        System.out.println();
        System.out.println("Exemplu:");
        System.out.println("input/vector_100000.txt");

        System.out.print("Calea fisierului: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("Nu ai introdus un fisier.");
            return;
        }

        int tests = readTests();

        List<BenchmarkResult> results =
                benchmarkService.runBenchmark(fileName, tests);

        allResults.addAll(results);

        ConsoleTable.print(results);
        ConsoleTable.printAverageResults(results);
    }

    private static void testAllFiles()
            throws IOException {

        int tests = readTests();

        List<BenchmarkResult> sessionResults =
                new ArrayList<>();

        for (int size : InputFileGenerator.SIZES) {

            String fileName =
                    "input/vector_" + size + ".txt";

            System.out.println();
            System.out.println("Fisier: " + fileName);

            try {

                List<BenchmarkResult> results =
                        benchmarkService.runBenchmark(fileName, tests);

                sessionResults.addAll(results);

            } catch (IOException e) {

                System.out.println(
                        "Nu s-a putut testa: " + fileName);
                System.out.println(e.getMessage());
            }
        }

        allResults.clear();
        allResults.addAll(sessionResults);

        ConsoleTable.print(allResults);
        ConsoleTable.printAverageResults(allResults);
    }

    private static void exportResults()
            throws IOException {

        if (allResults.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        CsvExporter.export(
                allResults, "rezultate/rezultate.csv");

        CsvExporter.exportAverages(
                allResults, "rezultate/timpi_medii.csv");
    }

    private static void generateChart()
            throws IOException {

        if (allResults.isEmpty()) {
            System.out.println("Nu exista rezultate.");
            return;
        }

        ChartGenerator.generate(
                allResults, "rezultate/grafic.svg");
    }

    private static void analyzeMemory() {

        System.out.println();
        System.out.println("===== ANALIZA MEMORIE =====");

        int size;

        do {
            size = readInt(
                    "Introdu numarul de elemente: ");

            if (size <= 0) {
                System.out.println(
                        "Introdu o dimensiune pozitiva.");
            }

        } while (size <= 0);

        MemoryAnalyzer.analyze(size);
        MemoryAnalyzer.analyzeThreshold();
    }

    private static int readTests() {

        int tests;

        do {

            tests = readInt(
                    "Introdu numarul de teste (1-100): ");

            if (tests < 1 || tests > 100) {
                System.out.println(
                        "Alege intre 1 si 100.");
            }

        } while (tests < 1 || tests > 100);

        return tests;
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Introdu un numar intreg valid.");
            }
        }
    }
}

