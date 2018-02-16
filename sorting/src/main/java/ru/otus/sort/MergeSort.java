package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public final class MergeSort implements SortStrategy {
    private static final int INSERTION_THRESHOLD = 2;
    private final SortStrategy insertionSort = new InsertionSort();

    @Override
    public <T extends Comparable> void sort(@NotNull T[] arr, int lo, int hi)  {
        sort(arr, Comparator.naturalOrder(), lo, hi);
    }

    @Override
    public <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int lo, int hi) {
        final int N = hi - lo;
        int middle = N / 2;
        if (N > INSERTION_THRESHOLD) {
            Thread threadA = new Thread(() -> sort(arr, comparator, lo, lo + middle - 1));
            Thread threadB = new Thread(() -> sort(arr, comparator, lo + middle, hi));
            threadA.start(); threadB.start();
            try {
                threadA.join();
                threadB.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SortUtils.merge2Arrays(arr, comparator, lo, lo + middle, hi);
        } else {
            insertionSort.sort(arr, comparator, lo, hi);
        }
    }

    @Override
    public void sort(@NotNull int[] arr, int lo, int hi) {
        final int N = hi - lo + 1;
        int middle = N / 2;
        if (N > INSERTION_THRESHOLD) {
            Thread threadA = new Thread(() -> sort(arr, lo, lo + middle-1));
            Thread threadB = new Thread(() -> sort(arr, lo + middle, hi));
            threadA.start(); threadB.start();
            try {
                threadA.join();
                threadB.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SortUtils.merge2Arrays(arr, lo, lo + middle, hi);
        } else {
            insertionSort.sort(arr, lo, hi);
        }
    }
}