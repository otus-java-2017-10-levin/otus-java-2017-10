package ru.otus.internals;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
class SummaryTest {

    private void checkSummary(TestSummary summary, int success, int failed, int ignore) {
        assertArrayEquals(new int[]{success,failed,ignore}, new int[]{summary.getSuccessTests(), summary.getFailedTests(), summary.getIgnoreTests()});
    }

    @Test
    void testCreation() {
        TestSummary summary = new TestSummary();
        checkSummary(summary, 0, 0, 0);
    }

    @Test
    void testAddition() {
        TestSummary summary = new TestSummary();

        summary.add(TestSummary.TYPE.SUCCESS, 1);
        checkSummary(summary, 1, 0, 0);

        summary.add(TestSummary.TYPE.FAILED, 1);
        checkSummary(summary, 1, 1, 0);

        summary.add(TestSummary.TYPE.IGNORE, 1);
        checkSummary(summary, 1, 1, 1);

        summary.add(2, 3, 1);
        checkSummary(summary, 3, 4, 2);
    }

    @Test
    void testAddition2() {
        TestSummary summary = new TestSummary();

        summary.add(1,0,1);
        checkSummary(summary, 1, 0, 1);

        TestSummary summary1 = new TestSummary();
        summary1.add(2, 5, 3);
        summary.add(summary1);
        checkSummary(summary, 3, 5, 4);
    }


    @Test()
    void failedAddition() {
        TestSummary summary = new TestSummary();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> summary.add(TestSummary.TYPE.SUCCESS, -1));

        exception = assertThrows(IllegalArgumentException.class, () -> summary.add(0, -1, 0));
        assertEquals(null, exception.getMessage());
    }


}