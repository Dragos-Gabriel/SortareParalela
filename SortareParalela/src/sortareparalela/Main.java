package sortareparalela;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int[] AVAILABLE_SIZES = {
            100_000,
            500_000,
            1_000_000,
            5_000_000
    };

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BenchmarkService benchmarkService =
                new BenchmarkService();

        List<BenchmarkResult> allResults =
                new ArrayList<>();

        boolean running = true;

        System.out.println();
        System.out.println("=================================================");
        System.out.println("       PROIECT - SORTARE IN PARALEL");
        System.out.println("=================================================");
        System.out.println("Java 17");
        System.out.println();

        System.out.println(
                "Numar procesoare disponibile: "
                + Runtime.getRuntime()
                .availableProcessors());

        while (running) {

            printMenu();

            int option =
                    readInt(
                            scanner,
                            "Alege optiunea: ");

            switch (option) {

                case 1 -> {

                    int size =
                            chooseSize(scanner);

                    int tests =
                            readNumberOfTests(scanner);

                    List<BenchmarkResult> results =
                            benchmarkService.runBenchmark(
                                    size,
                                    tests);

                    allResults.addAll(results);

                    ConsoleTable.print(results);

                    askForCsv(
                            scanner,
                            results,
                            "rezultate_sortare_" + size + ".csv");
                }

                case 2 -> {

                    int tests =
                            readNumberOfTests(scanner);

                    allResults.clear();

                    for (int size : AVAILABLE_SIZES) {

                        List<BenchmarkResult> results =
                                benchmarkService.runBenchmark(
                                        size,
                                        tests);

                        allResults.addAll(results);
                    }

                    System.out.println();
                    System.out.println(
                            "Toate testele au fost finalizate.");

                    ConsoleTable.print(allResults);

                    CsvExporter.export(
                            allResults,
                            "rezultate/rezultate_toate_dimensiunile.csv");
                }

                case 3 -> {

                    if (allResults.isEmpty()) {

                        System.out.println();
                        System.out.println(
                                "Nu exista rezultate.");
                        System.out.println(
                                "Ruleaza mai intai un benchmark.");

                    } else {

                        ConsoleTable.print(
                                allResults);

                        ConsoleTable.printAverageResults(
                                allResults);
                    }
                }

                case 4 -> {

                    if (allResults.isEmpty()) {

                        System.out.println();
                        System.out.println(
                                "Nu exista rezultate pentru CSV.");

                    } else {

                        String fileName =
                                readFileName(
                                        scanner,
                                        "Numele fisierului CSV: ");

                        CsvExporter.export(
                                allResults,
                                "rezultate/" + fileName);
                    }
                }

                case 5 -> {

                    if (allResults.isEmpty()) {

                        System.out.println();
                        System.out.println(
                                "Nu exista rezultate pentru grafic.");

                    } else {

                        String fileName =
                                readFileName(
                                        scanner,
                                        "Numele graficului: ");

                        ChartGenerator.generate(
                                allResults,
                                "rezultate/" + fileName);
                    }
                }

                case 6 -> {

                    if (allResults.isEmpty()) {

                        System.out.println();
                        System.out.println(
                                "Nu exista rezultate.");

                    } else {

                        ConsoleTable.printAverageResults(
                                allResults);
                    }
                }

                case 0 -> {

                    running = false;

                    System.out.println();
                    System.out.println(
                            "Aplicatia s-a inchis.");
                }

                default -> {

                    System.out.println();
                    System.out.println(
                            "Optiune invalida.");
                }
            }
        }

        scanner.close();
    }

    private static void printMenu() {

        System.out.println();
        System.out.println("--------------- MENIU ---------------");

        System.out.println(
                "1. Testeaza o singura dimensiune");

        System.out.println(
                "2. Testeaza toate dimensiunile");

        System.out.println(
                "3. Afiseaza toate rezultatele");

        System.out.println(
                "4. Salveaza rezultatele in CSV");

        System.out.println(
                "5. Genereaza grafic");

        System.out.println(
                "6. Afiseaza timpii medii");

        System.out.println(
                "0. Iesire");

        System.out.println(
                "------------------------------------");
    }

    private static int chooseSize(
            Scanner scanner) {

        System.out.println();
        System.out.println(
                "Alege numarul de elemente:");

        System.out.println(
                "1. 100.000");

        System.out.println(
                "2. 500.000");

        System.out.println(
                "3. 1.000.000");

        System.out.println(
                "4. 5.000.000");

        int option;

        while (true) {

            option =
                    readInt(
                            scanner,
                            "Optiunea: ");

            if (option >= 1 && option <= 4) {
                break;
            }

            System.out.println(
                    "Alege o optiune intre 1 si 4.");
        }

        return AVAILABLE_SIZES[option - 1];
    }

    private static int readNumberOfTests(
            Scanner scanner) {

        int tests;

        while (true) {

            tests =
                    readInt(
                            scanner,
                            "Numarul de teste: ");

            if (tests > 0 && tests <= 100) {
                break;
            }

            System.out.println(
                    "Numarul de teste trebuie sa fie "
                    + "intre 1 si 100.");
        }

        return tests;
    }

    private static int readInt(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        input.trim());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Introdu un numar valid.");
            }
        }
    }

    private static String readFileName(
            Scanner scanner,
            String message) {

        System.out.print(message);

        String fileName =
                scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            fileName = "rezultate.csv";
        }

        return fileName;
    }

    private static void askForCsv(
            Scanner scanner,
            List<BenchmarkResult> results,
            String defaultName) {

        System.out.println();
        System.out.print(
                "Vrei sa salvezi rezultatele in CSV? (d/n): ");

        String answer =
                scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("d")
                || answer.equalsIgnoreCase("da")) {

            CsvExporter.export(
                    results,
                    "rezultate/" + defaultName);
        }
    }
}
