package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;
import java.util.Queue;

final class SortUtils {

    public static <T extends Comparable<T>> boolean isSortedObj(T[] arr, boolean isAscending) {
        T prev = null;
        for (T i : arr) {
            if (prev != null && (isAscending ? i.compareTo(prev) < 0 : i.compareTo(prev) > 0))
                return false;
            prev = i;
        }
        return true;
    }

    public static boolean isSorted(int[] arr, boolean isAscending) {
        int prev = isAscending ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i : arr) {
            if (isAscending ? prev > i : prev < i)
                return false;
            prev = i;
        }
        return true;
    }

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

    public static void mergeKArrays(@NotNull int[] arr, int bagSize) {
        Queue<HeapItem> heap = new PriorityQueue<>();
        int[] res = new int[arr.length];

        final int bags = arr.length / bagSize;
        for (int i = 0; i < bags; i++) {
            final int pos = i * bagSize;
            heap.add(new HeapItem(arr[pos], i, pos));
        }

        int i = 0;
        while(!heap.isEmpty()) {
            HeapItem min = heap.remove();
            res[i++] = min.value;
            final int bound = Math.min((1+min.bag) * bagSize, arr.length);
            if (min.pos < bound-1) {
                heap.add(new HeapItem(arr[min.pos+1], min.bag, min.pos+1));
            }
        }
        System.arraycopy(res, 0, arr, 0, arr.length);
    }


    private static final class HeapItem implements Comparable<HeapItem> {
        private final int value;
        private final int bag;
        private final int pos;

        HeapItem(int value, int bag, int pos) {
            this.value = value;
            this.bag = bag;
            this.pos = pos;
        }

        @Override
        public int compareTo(@NotNull SortUtils.HeapItem o) {
            return Integer.compare(this.value, o.value);
        }
    }
}
