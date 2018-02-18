package ru.otus.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratorTest {

    @Test
    void reserveArray() {
        final int SIZE = 10_000;
        Integer[] arr = Generator.generateReversed(SIZE);
        assertEquals(true, SortUtils.isSortedObj(arr, false));
    }
}
