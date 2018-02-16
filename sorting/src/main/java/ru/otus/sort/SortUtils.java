package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Comparator;
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

    /**
     * Merge two sorted subarrays into sorted array
     * Return result in {@code arr}
     * @param arr - array with two subarrays
     * @param from - first element of the first sub array
     * @param middle - first element of the second array
     * @param to - element number after the last element of the second array
     */
    public static void merge2Arrays(@NotNull int[] arr, int from, int middle, int to) {
        int i=from, j=middle;
        int[] res = new int[to-from+1];

        int counter = 0;
        while (i < middle || j <= to) {
            int val;
            if (i == middle) {
                val = arr[j++];
            } else if (j == to+1) {
                val = arr[i++];
            }else
                val = arr[i] < arr[j] ? arr[i++] : arr[j++];

            res[counter++] = val;
        }
        System.arraycopy(res, 0, arr, from, res.length);
    }

    /**
     * Merge two sorted subarrays into sorted array
     * Create tmp array through reflection
     * Return result in {@code arr}
     * @param arr - array with two subarrays
     * @param from - first element of the first sub array
     * @param middle - first element of the second array
     * @param to - the last element of the second array
     */
    @SuppressWarnings("unchecked")
    public static <T> void merge2Arrays(@NotNull T[] arr,
                                        @NotNull Comparator<T> comparator,
                                        int from, int middle, int to) {
        int i=from, j=middle;

        T[] res = (T[])Array.newInstance(arr.getClass().getComponentType(), to-from+1);
        int counter = 0;
        while (i < middle || j <= to) {
            T val;
            if (i == middle) {
                val = arr[j++];
            } else if (j == to+1) {
                val = arr[i++];
            }else
                val = comparator.compare(arr[i], arr[j]) < 0 ? arr[i++] : arr[j++];

            res[counter++] = val;
        }
        System.arraycopy(res, 0, arr, from, res.length);
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

    public static <T> void mergeKArrays(T[] arr, Comparator<T> cmp, int bagSize) {
        Queue<HeapObject<T>> heap = new PriorityQueue<>();

        @SuppressWarnings("unchecked")
        T[] res = (T[])Array.newInstance(arr.getClass().getComponentType(), arr.length);

        final int bags = arr.length / bagSize;
        for (int i = 0; i < bags; i++) {
            final int pos = i * bagSize;
            heap.add(new HeapObject<>(arr[pos], cmp, i, pos));
        }

        int i = 0;
        while(!heap.isEmpty()) {
            HeapObject<T> min = heap.remove();
            res[i++] = min.value;
            final int bound = Math.min((1+min.bag) * bagSize, arr.length);
            if (min.pos < bound-1) {
                heap.add(new HeapObject<>(arr[min.pos+1], cmp, min.bag, min.pos+1));
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

    private static final class HeapObject<T> implements Comparable<HeapObject<T>> {
        private final T value;
        private final int bag;
        private final int pos;
        private final Comparator<T> cmp;

        HeapObject(T value, Comparator<T> cmp, int bag, int pos) {
            this.value = value;
            this.bag = bag;
            this.pos = pos;
            this.cmp = cmp;
        }


        @Override
        public int compareTo(@NotNull SortUtils.HeapObject<T> o) {
            return cmp.compare(this.value, o.value);
        }
    }
}