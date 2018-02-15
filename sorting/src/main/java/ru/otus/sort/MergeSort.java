package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public final class MergeSort implements SortStrategy {
    private static final int INSERTION_THRESHOLD = 2;
    private final SortStrategy insertionSort = new InsertionSort();

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int from, int to) {
        sort(arr, Comparator.naturalOrder(), from, to);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int from, int to) {
        final int N = to - from;
        if (N > INSERTION_THRESHOLD) {
            sort(arr, comparator, from, from+N / 2);
            sort(arr, comparator, from+N / 2, to);
        }
        SortUtils.merge2Arrays(arr, comparator, from, from+N/2, to);
    }

    @Override
    public void sort(@NotNull int[] arr, int from, int to) {
        final int N = to - from;
        if (N > INSERTION_THRESHOLD) {
            sort(arr, from, from+N / 2);
            sort(arr, from+N / 2, to);
        }
        SortUtils.merge2Arrays(arr, from, from+N/2, to);
    }
}