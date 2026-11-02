
package sortareparalela;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputFileGenerator {

    public static final int[] SIZES = {
            100_000,
            500_000,
            1_000_000,
            5_000_000,
            10_000_000,
            50_000_000,
            100_000_000,
            500_000_000  
    };

    private InputFileGenerator() {
    }

    public static void generateAll() throws IOException {

        Files.createDirectories(Path.of("input"));

        for (int size : SIZES) {

            String fileName =
                    "input/vector_" + size + ".txt";

            Path path = Path.of(fileName);

            if (Files.exists(path)) {
                System.out.println(
                        "Fisier existent, nu se suprascrie: "
                        + fileName);
                continue;
            }

            generateFile(size, fileName);
        }
    }

    public static void generateFile(
            int size, String fileName) throws IOException {

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Dimensiunea trebuie sa fie pozitiva.");
        }

        Path path = Path.of(fileName);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        try (BufferedWriter writer =
                Files.newBufferedWriter(path)) {

            writer.write(Integer.toString(size));
            writer.newLine();

            for (int i = 0; i < size; i++) {

                // Formula determinista, fara Random
                int value = (int) (
                        ((long) (size - i) * 31
                        + (long) (i % 997) * 17)
                        % 1_000_000);

                writer.write(Integer.toString(value));
                writer.newLine();
            }
        }

        System.out.println(
                "Fisier generat: " + path.toAbsolutePath());
    }
}

