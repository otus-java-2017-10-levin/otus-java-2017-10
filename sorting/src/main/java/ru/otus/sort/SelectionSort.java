package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Selection sort implementation.
 *
 * In place comparison sort with quadratic time complexity
 * Should be used on a small arrays.

 */
final class SelectionSort implements SortStrategy {

    @Override
    public void sort(@NotNull int[] arr, int from, int to) {
        final int length = to - from;
        if (length < 2)
            return;

        for (int i=from; i < to-1; i++) {
            for (int j = i+1; j<= to-1; j++) {
                int min = j;
                if (arr[min] > arr[j] )
                    min = j;

                if (arr[min] < arr[i])
                    SortUtils.swap(arr, i, min);
            }
        }
    }

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int from, int to) {
        sort0(arr, Comparator.naturalOrder(), from, to);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int from, int to) {
        sort0(arr, comparator, from, to);
    }

    private <T> void sort0(T[] arr, Comparator<T> comparator, int from, int to) {
        final int length = to - from;
        if (length < 2)
            return;

        for (int i = from; i < to - 1; i++) {
            for (int j = i + 1; j <= to - 1; j++) {
                int min = j;
                if (comparator.compare(arr[min], arr[j]) > 0)
                    min = j;

                if (comparator.compare(arr[min], arr[i]) < 0)
                    SortUtils.swap(arr, i, min);
            }
        }
    }
}