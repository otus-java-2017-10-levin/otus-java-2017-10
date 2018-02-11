package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Implementation of insertion sort algorithm
 * quadratic time complexity
 */
final class InsertionSort implements SortStrategy {
    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int from, int to) {
        sort0(arr, Comparator.naturalOrder(), from, to);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int from, int to) {
        sort0(arr, comparator, from, to);
    }

    @Override
    public void sort(@NotNull int[] arr, int from, int to) {
        if (arr.length < 2)
            return;

        for (int i = from; i <= to - 1; i++) {
            int j = i-1;
            final int key = arr[i];
            while (j >= from && arr[j] > key) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }

    private <T> void sort0(T[] arr, Comparator<T> comparator, int from, int to) {
        if (arr.length < 2)
            return;

        for (int i = from; i <= to - 1; i++) {
            int j = i-1;
            final T key = arr[i];
            while (j >= from && comparator.compare(arr[j], key) > 0) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }
}
