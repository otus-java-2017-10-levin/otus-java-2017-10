package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

final class QuickSort implements SortStrategy {
    private final int INSERTION_THRESHOLD;
    private static final SortStrategy sorter = new InsertionSort();

    QuickSort() {
        INSERTION_THRESHOLD = 47;
    }

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int lo, int hi) {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        if (lo < hi) {
            if (INSERTION_THRESHOLD > lo - hi) {
                sorter.sort(arr, comparator, lo, hi);
            } else {
                int p = partition(arr, comparator, lo, hi);
                new Thread(() -> sort(arr, comparator, lo, p - 1)).start();
                new Thread(() -> sort(arr, comparator, p + 1, hi)).start();
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

    @Override
    public void sort(@NotNull int[] arr, int lo, int hi) {
        if (lo < hi) {
            if (INSERTION_THRESHOLD > lo - hi) {
                sorter.sort(arr, lo, hi);
            } else {
                int p = partition(arr, lo, hi);
                new Thread(() ->sort(arr, lo, p - 1)).start();
                new Thread(() -> sort(arr, p + 1, hi)).start();
            }
        }
    }

    private int partition(int[] arr, int lo, int hi) {
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (arr[j] < arr[hi]) {
                i++;
                SortUtils.swap(arr, i, j);
            }
        }
        SortUtils.swap(arr, i + 1, hi);
        return i + 1;
    }
}