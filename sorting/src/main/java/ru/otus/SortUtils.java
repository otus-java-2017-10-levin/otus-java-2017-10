package ru.otus;

import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;

public final class SortUtils {

    public static void swap(@NotNull Object[] arr, int a1, int a2) {
        Object tmp = arr[a2];
        arr[a2] = arr[a1];
        arr[a1] = tmp;
    }

    public static void swap(@NotNull int[] arr, int a1, int a2) {
        int tmp = arr[a2];
        arr[a2] = arr[a1];
        arr[a1] = tmp;
    }
}
