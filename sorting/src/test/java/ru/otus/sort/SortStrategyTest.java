package ru.otus.sort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortStrategyTest {

    private static final int SIZE = 10_000;

    private static Stream<Arguments> sortedArgumentsProvider() {
        return Stream.of(
                Arguments.of(new InsertionSort()),
                Arguments.of(new QuickSort()),
                Arguments.of(new MergeSort())
                );
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void intTest(SortStrategy sort) {
        int[] original = Generator.generateArray(SIZE);
        sort.sort(original);
        assertEquals(true, SortUtils.isSorted(original, true));
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void sortObjects(SortStrategy sort) {
        Integer[] original = Generator.generateObjectArray(SIZE);
        sort.sort(original);
        assertEquals(true, SortUtils.isSortedObj(original, true));
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void testSortWithComparator(SortStrategy sort) {
        Integer[] original = Generator.generateObjectArray(SIZE);
        sort.sort(original, Comparator.reverseOrder());

        assertEquals(true, SortUtils.isSortedObj(original, false));
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void sortWithNullsFirst(SortStrategy sort) {
        Integer[] original = new Integer[]{5, null, 4, 3, null, 2, 1, 0, null};
        Integer[] expected = new Integer[]{null, null, null, 0, 1, 2, 3, 4, 5};

        sort.sort(original, Comparator.nullsFirst(Comparator.naturalOrder()));
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void sortWithNullsLast(SortStrategy sort) {
        Integer[] original = new Integer[]{5, null, 4, 3, null, 2, 1, 0, null};
        Integer[] expected = new Integer[]{0, 1, 2, 3, 4, 5, null, null, null};

        sort.sort(original, Comparator.nullsLast(Comparator.naturalOrder()));
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialSort(SortStrategy sort) {
        int[] original = {5, 4, 3, 2, 1, 0};
        int[] expected = {3, 4, 5, 2, 1, 0};

        sort.sort(original, 0, 2);
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialSort1(SortStrategy sort) {
        int[] original = {5, 4, 3, 2, 1, 0};
        int[] expected = {0, 1, 2, 3, 4, 5};

        sort.sort(original, 0, original.length-1);
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialSort2(SortStrategy sort) {
        int[] original = {5, 4, 33, 2, 1, 0};
        int[] expected = {4, 5, 33, 2, 1, 0};


        sort.sort(original, 0, 2);
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialObjectSort(SortStrategy sort) {
        Integer[] original = new Integer[]{5, 4, 3, 2, 1, 0};
        Integer[] expected = new Integer[]{3, 4, 5, 2, 1, 0};

        sort.sort(original, 0, 2);
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialObjectSort1(SortStrategy sort) {
        Integer[] original = new Integer[]{5, 4, 3, 2, 1, 0};
        Integer[] expected = new Integer[]{0, 1, 2, 3, 4, 5};

        sort.sort(original, 0, original.length-1);
        assertArrayEquals(expected, original);
    }

    @ParameterizedTest
    @MethodSource("sortedArgumentsProvider")
    void partialObjectSort2(SortStrategy sort) {
        Integer[] original = new Integer[]{5, 4, 33, 2, 1, 0};
        Integer[] expected = new Integer[]{4, 5, 33, 2, 1, 0};


        sort.sort(original, 0, 1);
        assertArrayEquals(expected, original);
    }
}