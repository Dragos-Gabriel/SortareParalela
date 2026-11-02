
package sortareparalela;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputFileReader {

    private InputFileReader() {
    }

    public static int readSize(String fileName)
            throws IOException {

        try (BufferedReader reader =
                Files.newBufferedReader(Path.of(fileName))) {

            String firstLine = reader.readLine();

            if (firstLine == null) {
                throw new IOException("Fisierul este gol.");
            }

            try {
                int size = Integer.parseInt(firstLine.trim());

                if (size <= 0) {
                    throw new IOException(
                            "Dimensiunea trebuie sa fie pozitiva.");
                }

                return size;

            } catch (NumberFormatException e) {
                throw new IOException(
                        "Prima linie trebuie sa contina dimensiunea.",
                        e);
            }
        }
    }

    public static int[] readVector(String fileName)
            throws IOException {

        int size = readSize(fileName);
        int[] vector = new int[size];

        try (BufferedReader reader =
                Files.newBufferedReader(Path.of(fileName))) {

            reader.readLine(); // Dimensiunea

            for (int i = 0; i < size; i++) {

                String line = reader.readLine();

                if (line == null) {
                    throw new IOException(
                            "Fisier incomplet. Lipseste elementul "
                            + (i + 1));
                }

                try {
                    vector[i] = Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    throw new IOException(
                            "Valoare invalida la elementul "
                            + (i + 1), e);
                }
            }

            if (reader.readLine() != null) {
                throw new IOException(
                        "Fisierul contine mai multe valori "
                        + "decat dimensiunea declarata.");
            }
        }

        System.out.println(
                "Vector incarcat din: " + fileName);

        System.out.println(
                "Numar elemente: " + size);

        return vector;
    }
}

