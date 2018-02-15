package ru.otus.sort;

import ru.otus.executors.ExecutorManager;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;

class ParallelSorterImpl implements ParallelSorter {

    private final SortStrategy strategy;
    private int threadNumber = 1;
    private ExecutorService service = ExecutorManager.getExecutor();

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
    public void sort(int[] arr) {
        if (threadNumber == 1) {
            strategy.sort(arr);
        } else {
            final int bagSize = arr.length / threadNumber;
            sortInParallel(arr, bagSize);
            try {
                ExecutorManager.shutdownAndAWaitTermination(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SortUtils.mergeKArrays(arr, bagSize);
        }
    }

    private void sortInParallel(int[] arr, int bagSize) {
        for (int i =0; i < threadNumber; i++) {
            final int from = i*bagSize;
            final int to = Math.min(from+bagSize, arr.length);
            service.execute(() -> strategy.sort(arr, from, to));
        }
    }
}
