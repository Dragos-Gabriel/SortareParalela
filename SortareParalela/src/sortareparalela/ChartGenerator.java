package sortareparalela;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartGenerator {

    private ChartGenerator() {
        // Clasă utilitară
    }

    public static void generate(
            List<BenchmarkResult> results,
            String fileName) {

        if (results == null || results.isEmpty()) {
            System.out.println(
                    "Nu exista date pentru generarea graficului.");
            return;
        }

        Map<Integer, List<Double>> sequential =
                new HashMap<>();

        Map<Integer, List<Double>> parallel =
                new HashMap<>();

        for (BenchmarkResult result : results) {

            Map<Integer, List<Double>> target;

            if (result.getMethod().equals("Arrays.sort()")) {
                target = sequential;
            } else {
                target = parallel;
            }

            target
                    .computeIfAbsent(
                            result.getNumberOfElements(),
                            key -> new ArrayList<>())
                    .add(result.getTimeMilliseconds());
        }

        List<Integer> sizes =
                new ArrayList<>(sequential.keySet());

        Collections.sort(sizes);

        List<Double> sequentialAverages =
                new ArrayList<>();

        List<Double> parallelAverages =
                new ArrayList<>();

        for (Integer size : sizes) {

            sequentialAverages.add(
                    average(sequential.get(size)));

            parallelAverages.add(
                    average(parallel.get(size)));
        }

        double maxTime = 0;

        for (Double value : sequentialAverages) {
            maxTime = Math.max(maxTime, value);
        }

        for (Double value : parallelAverages) {
            maxTime = Math.max(maxTime, value);
        }

        if (maxTime == 0) {
            maxTime = 1;
        }

        int width = 1000;
        int height = 600;

        int left = 100;
        int right = 50;
        int top = 70;
        int bottom = 100;

        int graphWidth = width - left - right;
        int graphHeight = height - top - bottom;

        File file = new File(fileName);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            writer.write("""
                    <svg xmlns="http://www.w3.org/2000/svg"
                         width="1000"
                         height="600"
                         viewBox="0 0 1000 600">
                    """);

            writer.write("""
                    <rect width="100%" height="100%" fill="white"/>
                    """);

            writer.write("""
                    <text x="500" y="35"
                          text-anchor="middle"
                          font-size="24"
                          font-family="Arial">
                    Comparatie Arrays.sort() vs Arrays.parallelSort()
                    </text>
                    """);

            // Axe
            writer.write(
                    "<line x1=\"" + left
                    + "\" y1=\"" + top
                    + "\" x2=\"" + left
                    + "\" y2=\"" + (height - bottom)
                    + "\" stroke=\"black\"/>");

            writer.write(
                    "<line x1=\"" + left
                    + "\" y1=\"" + (height - bottom)
                    + "\" x2=\"" + (width - right)
                    + "\" y2=\"" + (height - bottom)
                    + "\" stroke=\"black\"/>");

            // Etichete axe
            writer.write("""
                    <text x="25" y="300"
                          transform="rotate(-90 25 300)"
                          font-size="16"
                          font-family="Arial">
                    Timp (ms)
                    </text>
                    """);

            writer.write("""
                    <text x="500" y="580"
                          text-anchor="middle"
                          font-size="16"
                          font-family="Arial">
                    Numar de elemente
                    </text>
                    """);

            // Liniile orizontale
            for (int i = 0; i <= 5; i++) {

                double value =
                        maxTime * i / 5.0;

                int y =
                        (int) (
                                height
                                - bottom
                                - (value / maxTime)
                                * graphHeight
                        );

                writer.write(
                        "<line x1=\"" + left
                        + "\" y1=\"" + y
                        + "\" x2=\"" + (width - right)
                        + "\" y2=\"" + y
                        + "\" stroke=\"lightgray\"/>");

                writer.write(
                        "<text x=\""
                        + (left - 10)
                        + "\" y=\""
                        + (y + 5)
                        + "\" text-anchor=\"end\" "
                        + "font-size=\"12\" "
                        + "font-family=\"Arial\">"
                        + String.format(
                                java.util.Locale.US,
                                "%.1f",
                                value)
                        + "</text>");
            }

            if (sizes.size() == 1) {

                drawPoint(
                        writer,
                        left + graphWidth / 2,
                        getY(
                                sequentialAverages.get(0),
                                maxTime,
                                height,
                                bottom,
                                graphHeight),
                        "blue");

                drawPoint(
                        writer,
                        left + graphWidth / 2,
                        getY(
                                parallelAverages.get(0),
                                maxTime,
                                height,
                                bottom,
                                graphHeight),
                        "red");

            } else {

                // Desenam liniile
                for (int i = 0; i < sizes.size() - 1; i++) {

                    int x1 =
                            getX(
                                    i,
                                    sizes.size(),
                                    left,
                                    graphWidth);

                    int x2 =
                            getX(
                                    i + 1,
                                    sizes.size(),
                                    left,
                                    graphWidth);

                    int y1 =
                            getY(
                                    sequentialAverages.get(i),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    int y2 =
                            getY(
                                    sequentialAverages.get(i + 1),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    writer.write(
                            "<line x1=\"" + x1
                            + "\" y1=\"" + y1
                            + "\" x2=\"" + x2
                            + "\" y2=\"" + y2
                            + "\" stroke=\"blue\" "
                            + "stroke-width=\"3\"/>");

                    y1 =
                            getY(
                                    parallelAverages.get(i),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    y2 =
                            getY(
                                    parallelAverages.get(i + 1),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    writer.write(
                            "<line x1=\"" + x1
                            + "\" y1=\"" + y1
                            + "\" x2=\"" + x2
                            + "\" y2=\"" + y2
                            + "\" stroke=\"red\" "
                            + "stroke-width=\"3\"/>");
                }

                // Puncte
                for (int i = 0; i < sizes.size(); i++) {

                    int x =
                            getX(
                                    i,
                                    sizes.size(),
                                    left,
                                    graphWidth);

                    int ySequential =
                            getY(
                                    sequentialAverages.get(i),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    int yParallel =
                            getY(
                                    parallelAverages.get(i),
                                    maxTime,
                                    height,
                                    bottom,
                                    graphHeight);

                    drawPoint(
                            writer,
                            x,
                            ySequential,
                            "blue");

                    drawPoint(
                            writer,
                            x,
                            yParallel,
                            "red");

                    writer.write(
                            "<text x=\"" + x
                            + "\" y=\""
                            + (height - bottom + 25)
                            + "\" text-anchor=\"middle\" "
                            + "font-size=\"12\" "
                            + "font-family=\"Arial\">"
                            + sizes.get(i)
                            + "</text>");
                }
            }

            // Legenda
            writer.write("""
                    <circle cx="750" cy="75" r="6" fill="blue"/>
                    <text x="765" y="80"
                          font-size="14"
                          font-family="Arial">
                    Arrays.sort()
                    </text>
                    """);

            writer.write("""
                    <circle cx="750" cy="100" r="6" fill="red"/>
                    <text x="765" y="105"
                          font-size="14"
                          font-family="Arial">
                    Arrays.parallelSort()
                    </text>
                    """);

            writer.write("</svg>");

            System.out.println();
            System.out.println(
                    "Grafic SVG generat: "
                    + file.getAbsolutePath());

        } catch (IOException e) {

            System.err.println(
                    "Eroare la generarea graficului.");

            e.printStackTrace();
        }
    }

    private static double average(
            List<Double> values) {

        if (values == null || values.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    private static int getX(
            int index,
            int count,
            int left,
            int graphWidth) {

        if (count == 1) {
            return left + graphWidth / 2;
        }

        return left
                + index * graphWidth / (count - 1);
    }

    private static int getY(
            double value,
            double maxTime,
            int height,
            int bottom,
            int graphHeight) {

        return (int) (
                height
                - bottom
                - (value / maxTime)
                * graphHeight
        );
    }

    private static void drawPoint(
            BufferedWriter writer,
            int x,
            int y,
            String color)
            throws IOException {

        writer.write(
                "<circle cx=\"" + x
                + "\" cy=\"" + y
                + "\" r=\"6\" fill=\""
                + color
                + "\"/>");
    }
}
