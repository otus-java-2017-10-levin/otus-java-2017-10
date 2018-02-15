package ru.otus.sort;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class InsertionSortTest {
    private static final int SIZE = 1_000;
    private SortStrategy sort = new InsertionSort();

    @Test
    void intTest() {
        int[] original = Generator.generateArray(SIZE);
        sort.sort(original);
        assertEquals(true, SortUtils.isSorted(original, true));
    }

    @Test
    void sortObjects() {
        Integer[] original = Generator.generateObjectArray(SIZE);
        sort.sort(original);
        assertEquals(true, SortUtils.isSortedObj(original, true));
    }

    @Test
    void testSortWithComparator() {
        Integer[] original = Generator.generateObjectArray(SIZE);
        sort.sort(original, Comparator.reverseOrder());

        assertEquals(true, SortUtils.isSortedObj(original, false));
    }

    @Test
    void sortWithNullsFirst() {
        Integer[] original = new Integer[]{5,null,4,3,null,2,1,0, null};
        Integer[] expected = new Integer[]{null,null,null, 0, 1, 2, 3, 4, 5};

        sort.sort(original, Comparator.nullsFirst(Comparator.naturalOrder()));
        assertArrayEquals(expected, original);
    }

    @Test
    void sortWithNullsLast() {
        Integer[] original = new Integer[]{5,null,4,3,null,2,1,0, null};
        Integer[] expected = new Integer[]{0, 1, 2, 3, 4, 5, null,null,null};

        sort.sort(original, Comparator.nullsLast(Comparator.naturalOrder()));
        assertArrayEquals(expected, original);
    }

    @Nested
    class PartialSort {
        @Test
        void partialSort() {
            int[] original = {5, 4, 3, 2, 1, 0};
            int[] expected = {3, 4, 5, 2, 1, 0};

            sort.sort(original, 0, 3);
            assertArrayEquals(expected, original);
        }

        @Test
        void partialSort1() {
            int[] original = {5, 4, 3, 2, 1, 0};
            int[] expected = {0, 1, 2, 3, 4, 5};

            sort.sort(original, 0, original.length);
            assertArrayEquals(expected, original);
        }

        @Test
        void partialSort2() {
            int[] original = {5, 4, 33, 2, 1, 0};
            int[] expected = {4, 5, 33, 2, 1, 0};


            sort.sort(original, 0, 2);
            assertArrayEquals(expected, original);
        }

        @Test
        void partialObjectSort() {
            Integer[] original = new Integer[]{5, 4, 3, 2, 1, 0};
            Integer[] expected = new Integer[]{3, 4, 5, 2, 1, 0};

            sort.sort(original, 0, 3);
            assertArrayEquals(expected, original);
        }

        @Test
        void partialObjectSort1() {
            Integer[] original = new Integer[]{5, 4, 3, 2, 1, 0};
            Integer[] expected = new Integer[]{0, 1, 2, 3, 4, 5};

            sort.sort(original, 0, original.length);
            assertArrayEquals(expected, original);
        }

        @Test
        void partialObjectSort2() {
            Integer[] original = new Integer[]{5, 4, 33, 2, 1, 0};
            Integer[] expected = new Integer[]{4, 5, 33, 2, 1, 0};


            sort.sort(original, 0, 2);
            assertArrayEquals(expected, original);
        }
    }
}