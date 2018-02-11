package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class SortManager {

    public enum SortType {
        SELECTION,
        INSERTION,
        SHELL,
        MERGE,
        QSORT
    }

    @NotNull
    public static SortStrategy getSorter(SortType type) {
        if (!sorters.containsKey(type))
            throw new IllegalArgumentException("SortType " + type+" is not supported");

        return sorters.get(type);
    }

    private static final Map<SortType, SortStrategy> sorters = new HashMap<>();

    static {
        sorters.put(SortType.SELECTION, new SelectionSort());
        sorters.put(SortType.INSERTION, new InsertionSort());
        sorters.put(SortType.SHELL, new ShellSort());
    }
}
