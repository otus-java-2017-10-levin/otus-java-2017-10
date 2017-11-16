package ru.otus.internals;

public class TestSummary {

    private int ignoreTests;
    private int successTests;
    private int failedTests;

    public enum TYPE {
        IGNORE,
        SUCCESS,
        FAILED
    }

    public int getIgnoreTests() {
        return ignoreTests;
    }

    public int getSuccessTests() {
        return successTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public TestSummary add(int successTests, int failedTests, int ignoreTests) throws IllegalArgumentException {
        if (!checkNotNegative(successTests, failedTests, ignoreTests))
            throw new IllegalArgumentException();

            this.successTests += successTests;
            this.failedTests += failedTests;
            this.ignoreTests += ignoreTests;
            return this;
    }

    public TestSummary add(TYPE type, int value) throws IllegalArgumentException {
        if (checkNotNegative(value)) {
            if (type == TYPE.IGNORE) {
                ignoreTests += value;
            } else if (type == TYPE.SUCCESS) {
                successTests += value;
            } else if (type == TYPE.FAILED) {
                failedTests += value;
            }
        }  else
            throw new IllegalArgumentException();

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public TestSummary add(TestSummary testSummary) {
        this.failedTests += testSummary.getFailedTests();
        this.ignoreTests += testSummary.getIgnoreTests();
        this.successTests += testSummary.getSuccessTests();
        return this;
    }

    public String toString() {
        return String.format("Success: %d; Failed: %d; Ignore: %d", successTests, failedTests, ignoreTests);
    }

    private boolean checkNotNegative(int... nums) {

        if (nums.length >0) {
            for (int i: nums) {
                if (i < 0)
                    return false;
            }
        }
        return true;
    }
}
