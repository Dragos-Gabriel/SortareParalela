package sortareparalela;

public class BenchmarkResult {

    private final String inputFile;
    private final int numberOfElements;
    private final int testNumber;
    private final String method;
    private final long timeNanoseconds;

    public BenchmarkResult(
            String inputFile,
            int numberOfElements,
            int testNumber,
            String method,
            long timeNanoseconds) {

        this.inputFile = inputFile;
        this.numberOfElements = numberOfElements;
        this.testNumber = testNumber;
        this.method = method;
        this.timeNanoseconds = timeNanoseconds;
    }

    public String getInputFile() {
        return inputFile;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public String getMethod() {
        return method;
    }

    public long getTimeNanoseconds() {
        return timeNanoseconds;
    }

    public double getTimeMilliseconds() {
        return timeNanoseconds / 1_000_000.0;
    }
}
