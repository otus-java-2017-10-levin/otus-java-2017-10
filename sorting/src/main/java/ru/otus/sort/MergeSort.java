package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class MergeSort implements SortStrategy {
    private static final int INSERTION_THRESHOLD = 47;
    private final SortStrategy insertionSort = new InsertionSort();

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int lo, int hi)  {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkObjectSort<T>(lo, hi, arr, comparator));
    }

    @Override
    public void sort(@NotNull int[] arr, int lo, int hi) {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkSort(lo, hi, arr));
    }

    private class ForkSort extends RecursiveAction {

        private final int lo;
        private final int hi;
        private final int[] arr;

        ForkSort(int lo, int hi, int[] arr) {
            this.lo = lo;
            this.hi = hi;
            this.arr = arr;
        }

        @Override
        protected void compute() {
            final int N = hi - lo + 1;
            int middle = N / 2;
            if (N > INSERTION_THRESHOLD) {
                invokeAll(new ForkSort(lo, lo + middle-1, arr),
                        new ForkSort(lo+middle, hi, arr));
                SortUtils.merge2Arrays(arr, lo, lo + middle, hi);
            } else {
                insertionSort.sort(arr, lo, hi);
            }
        }
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