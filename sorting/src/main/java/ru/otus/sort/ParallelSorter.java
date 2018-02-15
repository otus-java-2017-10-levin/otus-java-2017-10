package ru.otus.sort;

import java.util.Comparator;

/**
 *  Sorts an array in several threads.
 *  Sorting algorithm is selected in implementation constructor
 *
 *  Interface consist of 3 sort methods:
 *  1. {@link ParallelSorter#sort(int[])}sort primitive types in natural order.
 *  2. {@link ParallelSorter#sort(Comparable[])} sort object array with Comparable interface
 *  3. {@link ParallelSorter#sort(Object[], Comparator)} sort objects array with user comparator
 *
 *  If threads() is set to 1 - sorting whole array sequentially.
 *  Else split array into thread number parts, and sorting in parallel.
 *
 *
 */
public interface ParallelSorter {

    /**
     * Sets number of threads for sorting.
     * @param num - number of threads. num must be > 0
     * @return - sorter object.
     */
    ParallelSorter threads(int num);

    /**
     * Sort array of object with the specified algorithm
     * @param arr - array to sort
     * @param <T> - element type
     */
    <T extends Comparable> void sort(T[] arr);

    /**
     * Sort array of object with the specified algorithm
     * @param arr - array to sort
     * @param cmp - Comparator
     * @param <T> - element type
     */
    <T> void sort(T[] arr, Comparator<T> cmp);

    /**
     * Sort array of primitive ints with the specified algorithm
     * @param arr - array to sort
     */
    void sort(int[] arr);
}
