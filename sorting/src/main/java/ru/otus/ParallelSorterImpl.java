package ru.otus;

import ru.otus.sort.SortStrategy;

import java.util.Comparator;

public class ParallelSorterImpl implements ParallelSorter {

    private final SortStrategy strategy;
    private int threadNumber = 1;

    public ParallelSorterImpl(SortStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public ParallelSorter threads(int num) {
        if (num < 1)
            throw new IllegalArgumentException();

        threadNumber = num;
        return this;
    }

    @Override
    public <T extends Comparable> void sort(T[] arr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void sort(T[] arr, Comparator<T> cmp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(int[] arr) throws Exception {
        if (threadNumber == 1) {
            strategy.sort(arr);
        } else {
            throw new Exception("not supported.. yet");
        }
    }
}
