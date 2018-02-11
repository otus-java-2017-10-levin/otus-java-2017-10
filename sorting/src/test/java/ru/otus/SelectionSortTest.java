package ru.otus;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SelectionSortTest {

    private final SortStrategy sort = SortManager.getSorter(SortManager.SortType.SELECTION);

    @Test
    void intTest() {
        int[] original = {5, 4, 3, 2, 1, 0};
        int[] expected = {0, 1, 2, 3, 4, 5};
        sort.sort(original);
        assertArrayEquals(expected, original);
    }

    @Test
    void sortObjects() {
        Integer[] original = {5, 4, 3, 2, 1, 0};
        Integer[] expected = {0, 1, 2, 3, 4, 5};

        sort.sort(original);
        assertArrayEquals(expected, original);
    }

    @Test
    void testSortWithComparator() {
        Integer[] original = {5, 4, 3, 2, 1, 0};
        sort.sort(original, Comparator.reverseOrder());

        assertArrayEquals(new Integer[]{5, 4, 3, 2, 1, 0}, original);
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
}