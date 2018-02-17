package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.concurrent.*;

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
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new QSortFork<>(lo, hi, arr, Comparator.naturalOrder()));
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            forkJoinPool.shutdownNow();
            e.printStackTrace();
        }
    }

    @Override
    public <T> void sort(@NotNull T[] arr,
                         @NotNull Comparator<T> comparator,
                         int lo, int hi) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new QSortFork<>(lo, hi, arr, comparator));
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            forkJoinPool.shutdownNow();
            e.printStackTrace();
        }
    }

    private class QSortFork<T> extends RecursiveAction {
        private final int lo;
        private final int hi;
        private final T[] arr;
        private final Comparator<T> cmp;

        QSortFork(int lo, int hi, T[] arr, Comparator<T> cmp) {
            this.lo = lo;
            this.hi = hi;
            this.arr = arr;
            this.cmp = cmp;
        }

        @Override
        protected void compute() {
            if (lo < hi) {
                if (INSERTION_THRESHOLD > hi - lo) {
                    sorter.sort(arr, cmp, lo, hi);
                } else {
                    int p = partition(arr, cmp, lo, hi);
                    new QSortFork<>(lo, p-1, arr, cmp).fork();
                    new QSortFork<>(p+1, hi, arr, cmp).fork();
                }
            }
        }

        private int partition(@NotNull T[] arr,
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
}