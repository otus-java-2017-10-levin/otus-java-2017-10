package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Comparator;

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
}