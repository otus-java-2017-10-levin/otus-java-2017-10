package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

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
    public <T extends Comparable<T>> void sort(@NotNull T[] arr, int lo, int hi)  {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkObjectSort<>(lo, hi, arr, comparator));
    }

    private class ForkObjectSort<T> extends RecursiveAction {
        private final int lo;
        private final int hi;
        private final T[] arr;
        private final Comparator<T> cmp;

        ForkObjectSort(int lo, int hi, T[] arr, Comparator<T> cmp) {
            this.lo = lo;
            this.hi = hi;
            this.arr = arr;
            this.cmp = cmp;
        }

        @Override
        protected void compute() {
            final int N = hi - lo;
            int middle = N / 2;
            if (N > INSERTION_THRESHOLD) {
                invokeAll(new ForkObjectSort<>(lo, lo + middle - 1, arr, cmp),
                new ForkObjectSort<>(lo + middle, hi, arr, cmp));

                SortUtils.merge2Arrays(arr, cmp, lo, lo + middle, hi);
            } else {
                insertionSort.sort(arr, cmp, lo, hi);
            }
        }
    }
}