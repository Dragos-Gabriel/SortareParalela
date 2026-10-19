package sortareparalela;

public class BenchmarkResult {

    private final int numberOfElements;
    private final int testNumber;
    private final String method;
    private final long timeNanoseconds;

    public BenchmarkResult(
            int numberOfElements,
            int testNumber,
            String method,
            long timeNanoseconds) {

        this.numberOfElements = numberOfElements;
        this.testNumber = testNumber;
        this.method = method;
        this.timeNanoseconds = timeNanoseconds;
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

    public double getTimeSeconds() {
        return timeNanoseconds / 1_000_000_000.0;
    }
}
