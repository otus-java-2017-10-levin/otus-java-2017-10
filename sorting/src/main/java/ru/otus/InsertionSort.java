package ru.otus;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Implementation of insertion sort algorithm
 * quadratic time complexity
 */
final class InsertionSort implements SortStrategy {
    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr) {
        sort0(arr, Comparator.naturalOrder());
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator) {
        sort0(arr, comparator);
    }

    @Override
    public void sort(@NotNull int[] arr) {
        if (arr.length < 2)
            return;

        final int length = arr.length;
        for (int i = 1; i <= length - 1; i++) {
            int j = i-1;
            final int key = arr[i];
            while (j >= 0 && arr[j] > key) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }

    private <T> void sort0(T[] arr, Comparator<T> comparator) {
        if (arr.length < 2)
            return;

        final int length = arr.length;
        for (int i = 1; i <= length - 1; i++) {
            int j = i-1;
            final T key = arr[i];
            while (j >= 0 && comparator.compare(arr[j], key) > 0) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }
}
