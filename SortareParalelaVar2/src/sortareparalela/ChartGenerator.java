
package sortareparalela;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartGenerator {

    private ChartGenerator() {
    }

    public static void generate(
            List<BenchmarkResult> results,
            String fileName) throws IOException {

        if (results.isEmpty()) {
            System.out.println("Nu exista date pentru grafic.");
            return;
        }

        Map<Integer, Map<String, Double>> averages =
                ConsoleTable.calculateAverages(results);

        int width = 1000;
        int height = 600;

        int left = 100;
        int right = 60;
        int top = 100;
        int bottom = 100;

        int graphWidth = width - left - right;
        int graphHeight = height - top - bottom;

        double max = 1;

        for (var item : averages.values()) {
            for (double value : item.values()) {
                max = Math.max(max, value);
            }
        }

        max *= 1.10;

        Path path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        try (BufferedWriter writer =
                Files.newBufferedWriter(path)) {

            writer.write("""
                    <svg xmlns="http://www.w3.org/2000/svg"
                         width="1000" height="600">
                    <rect width="1000" height="600" fill="white"/>
                    <text x="500" y="35" text-anchor="middle"
                          font-family="Arial" font-size="24">
                    Sortare secventiala vs paralela
                    </text>
                    """);

            int baseY = height - bottom;

            // Grila si etichetele axei Y
            for (int i = 0; i <= 5; i++) {

                double value = max * i / 5;
                int y = baseY - graphHeight * i / 5;

                writer.write(String.format(Locale.US,
                        "<line x1='%d' y1='%d' x2='%d' y2='%d' "
                        + "stroke='#dddddd'/>%n",
                        left, y, width - right, y));

                writer.write(String.format(Locale.US,
                        "<text x='%d' y='%d' text-anchor='end' "
                        + "font-family='Arial' font-size='12'>%.1f</text>%n",
                        left - 10, y + 4, value));
            }

            writer.write(String.format(
                    "<line x1='%d' y1='%d' x2='%d' y2='%d' "
                    + "stroke='black' stroke-width='2'/>",
                    left, top, left, baseY));

            writer.write(String.format(
                    "<line x1='%d' y1='%d' x2='%d' y2='%d' "
                    + "stroke='black' stroke-width='2'/>",
                    left, baseY, width - right, baseY));

            String[] methods = {
                    "Arrays.sort()",
                    "Arrays.parallelSort()"
            };

            String[] colors = {"blue", "red"};

            int count = averages.size();

            for (int m = 0; m < methods.length; m++) {

                StringBuilder points = new StringBuilder();
                int index = 0;

                for (var entry : averages.entrySet()) {

                    int x = count == 1
                            ? left + graphWidth / 2
                            : left + index * graphWidth / (count - 1);

                    Double value = entry.getValue().get(methods[m]);

                    if (value != null) {

                        int y = baseY
                                - (int) (value / max * graphHeight);

                        points.append(x).append(",")
                              .append(y).append(" ");

                        writer.write(String.format(Locale.US,
                                "<circle cx='%d' cy='%d' r='6' "
                                + "fill='%s'/>%n",
                                x, y, colors[m]));
                    }

                    if (m == 0) {
                        writer.write(String.format(
                                "<text x='%d' y='%d' text-anchor='middle' "
                                + "font-size='12' font-family='Arial'>%d</text>%n",
                                x, baseY + 25, entry.getKey()));
                    }

                    index++;
                }

                writer.write(
                        "<polyline fill='none' stroke='"
                        + colors[m]
                        + "' stroke-width='3' points='"
                        + points.toString().trim()
                        + "'/>");
            }

            writer.write("""
                    <text x="500" y="570" text-anchor="middle"
                          font-family="Arial" font-size="16">
                    Numar de elemente
                    </text>
                    <text x="30" y="300" text-anchor="middle"
                          transform="rotate(-90 30 300)"
                          font-family="Arial" font-size="16">
                    Timp mediu (ms)
                    </text>
                    <line x1="690" y1="65" x2="730" y2="65"
                          stroke="blue" stroke-width="3"/>
                    <text x="740" y="70" font-family="Arial"
                          font-size="14">Arrays.sort()</text>
                    <line x1="690" y1="90" x2="730" y2="90"
                          stroke="red" stroke-width="3"/>
                    <text x="740" y="95" font-family="Arial"
                          font-size="14">Arrays.parallelSort()</text>
                    </svg>
                    """);
        }

        System.out.println(
                "Grafic salvat: " + path.toAbsolutePath());
    }
}

