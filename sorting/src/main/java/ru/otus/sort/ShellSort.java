package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Modification of selection sort
 * Sort array with gap sequence
 */
class ShellSort implements SortStrategy {

    private final int[] gaps = {701, 301, 132, 57, 23, 10, 4, 1};

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
        final int N = to - from;

        int h = 1;
        int k = 1;
        while (h < N) h = ((int) Math.pow(3, k++) - 1) / 2;

        h /=3;

        if (N < 2)
            return;
        while (h > 0) {
            for (int i = from + h; i <= from + N - 1; i += 1) {
                final int temp = arr[i];
                int j;
                for (j = i; j >= from+h && arr[j - h] > temp; j -= h) {
                    SortUtils.swap(arr, j, j - h);
                }
                arr[j] = temp;
            }
            h = h / 3;
        }
    }

    private <T> void sort0(T[] arr, Comparator<T> cmp, int from, int to) {
        final int N = to - from;

        if (N < 2)
            return;
        int h = 1;
        while (h < N / 3) h = 3 * h + 1;

        while (h >= 1) {
            for (int i = h; i <= N - 1; i += 1) {
                final T temp = arr[i];
                int j;
                for (j = i; j >= h && cmp.compare(arr[j - h], temp) > 0; j -= h) {
                    arr[j] = arr[j - h];
                }
                arr[j] = temp;
            }
            h /= 3;
        }
    }
}
