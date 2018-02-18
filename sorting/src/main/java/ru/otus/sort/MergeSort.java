package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


public final class MergeSort implements SortStrategy {
    private final int INSERTION_THRESHOLD;
    private final SortStrategy insertionSort = new InsertionSort();

    public MergeSort() {
        INSERTION_THRESHOLD = 47;
    }

    public MergeSort(int INSERTION_THRESHOLD) {
        this.INSERTION_THRESHOLD = INSERTION_THRESHOLD;
    }

    @Override
    public <T extends Comparable<T>> void sort(@NotNull T[] arr, int lo, int hi) {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        final int N = hi - lo;
        int middle = N / 2;
        if (N > INSERTION_THRESHOLD) {
            sort(arr, comparator, lo, lo + middle - 1);
            sort(arr, comparator, lo + middle, hi);

            SortUtils.merge2Arrays(arr, comparator, lo, lo + middle, hi);
        } else {
            insertionSort.sort(arr, comparator, lo, hi);
        }
    }
}
