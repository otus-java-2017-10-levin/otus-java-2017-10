package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

final class QuickSort implements SortStrategy {
    private static final SortStrategy sorter = new InsertionSort();
    private final int INSERTION_THRESHOLD;

    public QuickSort() {
        INSERTION_THRESHOLD = 47;
    }

    public QuickSort(int INSERTION_THRESHOLD) {
        this.INSERTION_THRESHOLD = INSERTION_THRESHOLD;
    }

    @Override
    public <T extends Comparable<T>> void sort(@NotNull T[] arr, int lo, int hi) {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr,
                         @NotNull Comparator<T> comparator,
                         int lo, int hi) {
        if (lo < hi) {
            if (INSERTION_THRESHOLD > hi - lo) {
                sorter.sort(arr, comparator, lo, hi);
            } else {
                int p = partition(arr, comparator, lo, hi);
                sort(arr, comparator, lo, p - 1);
                sort(arr, comparator, p + 1, hi);
            }
        }

    }

    private <T> int partition(@NotNull T[] arr,
                              @NotNull Comparator<T> comparator,
                              int lo, int hi) {
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (comparator.compare(arr[j], arr[hi]) < 0) {
                i++;
                SortUtils.swap(arr, i, j);
            }
        }
        SortUtils.swap(arr, i + 1, hi);
        return i + 1;
    }
}