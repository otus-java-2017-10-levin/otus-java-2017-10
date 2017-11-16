package ru.otus.internals;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssertTest {


    @Test
    public void testAssertTrue() {
        Assert.assertTrue(true);
        assertThrows(RuntimeException.class, () -> Assert.assertTrue(false));

    }

    @Test
    public void testAssertEquals() {
        final List<Integer> col1 = Arrays.asList(1, 2, 3);
        final List<Integer> col2 = Arrays.asList(1, 2, 4);

       assertThrows(RuntimeException.class, () -> Assert.assertEquals(col1, col2));

        List<Integer> col3 = Arrays.asList(1, 2, 3);
        Assert.assertEquals(col1, col3);
    }
}