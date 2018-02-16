package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


final class InsertionSort implements SortStrategy {
    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int lo, int hi) {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        if (arr.length < 2)
            return;

        for (int i = lo; i <= hi; i++) {
            int j = i-1;
            final T key = arr[i];
            while (j >= lo && comparator.compare(arr[j], key) > 0) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }

    @Override
    public void sort(@NotNull int[] arr, int lo, int hi) {
        if (arr.length < 2)
            return;

        for (int i = lo; i <= hi; i++) {
            int j = i-1;
            final int key = arr[i];
            while (j >= lo && arr[j] > key) {
                SortUtils.swap(arr, j+1, j);
                j--;
            }
            arr[j+1] = key;
        }
    }
}
