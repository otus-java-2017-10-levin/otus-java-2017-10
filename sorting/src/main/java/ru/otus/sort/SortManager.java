package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public final class SortManager {

    @SuppressWarnings("unused")
    @NotNull
    public static SortStrategy getSorter(SortType type, int insertionThreshold) {

        if (type == SortType.INSERTION)
            return new InsertionSort();
        if(type == SortType.PARALLEL_MERGE)
            return new ParallelMergeSort(insertionThreshold);
        if (type == SortType.MERGE)
            return new MergeSort(insertionThreshold);
        if(type == SortType.QSORT)
            return new QuickSort(insertionThreshold);

        return new ParallelQuickSort(insertionThreshold);
    }
}