package ru.otus.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortUtilsTest {

    @Test
    void isSorted() {
        int[] arrAsc = {0, 1, 2, 3};
        int[] arrDesc = {3, 2, 1, 0};
        int[] arrEqualsAsc = {1, 1, 2, 2};
        int[] arrEqualsDesc = {2, 2, 1, 1};

        assertEquals(true, SortUtils.isSorted(arrAsc, true));
        assertEquals(true, SortUtils.isSorted(arrEqualsAsc, true));
        assertEquals(true, SortUtils.isSorted(arrDesc, false));
        assertEquals(true, SortUtils.isSorted(arrEqualsDesc, false));
    }

    @Test
    void isSortedObjects() {
        Integer[] arrAsc = {0, 1, 2, 3};
        Integer[] arrDesc = {3, 2, 1, 0};
        Integer[] arrEqualsAsc = {1, 1, 2, 2};
        Integer[] arrEqualsDesc = {2, 2, 1, 1};

        assertEquals(true, SortUtils.isSortedObj(arrAsc, true));
        assertEquals(true, SortUtils.isSortedObj(arrEqualsAsc, true));
        assertEquals(true, SortUtils.isSortedObj(arrDesc, false));
        assertEquals(true, SortUtils.isSortedObj(arrEqualsDesc, false));
    }

}