package ru.otus;

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
    public void sort(@NotNull int[] arr) {
        final int length = arr.length;
        if (length < 2)
            return;

        for (int i=0; i < length-1; i++) {
            for (int j = i+1; j<= length-1; j++) {
                int min = j;
                if (arr[min] > arr[j] )
                    min = j;

                if (arr[min] < arr[i])
                    SortUtils.swap(arr, i, min);
            }
        }
    }

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr) {
        sort0(arr, Comparator.naturalOrder());
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator) {
        sort0(arr, comparator);
    }

    private <T> void sort0(T[] arr, Comparator<T> comparator) {
        final int length = arr.length;
        if (length < 2)
            return;

        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j <= length - 1; j++) {
                int min = j;
                if (comparator.compare(arr[min], arr[j]) > 0)
                    min = j;

                if (comparator.compare(arr[min], arr[i]) < 0)
                    SortUtils.swap(arr, i, min);
            }
        }
    }
}