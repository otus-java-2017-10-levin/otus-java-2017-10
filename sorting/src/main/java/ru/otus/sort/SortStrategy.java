package ru.otus.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Contains two main methods for sorting comparable objects, and primitive integers.
 * Sorting only ints is for simplicity.
 *
 * Contains methods for partial sort. This is useful when sorting array in several threads
 */
public interface SortStrategy {

    /**
     * Sort array of objects.
     * If array contains null element, then the NullPointerException will be thrown
     * @param arr - array to sort
     * @param <T> - comparable type
     */
    default <T extends Comparable> void sort(@NotNull T[] arr) {
        sort(arr, 0, arr.length);
    }

    /**
     * Sort object array with comparator
     * Array of T may contain nulls.
     * nulls may be considered less then non-null {@link Comparator#nullsFirst(Comparator)},
     * or greater then non-null {@link Comparator#nullsLast(Comparator)}.
     *
     * @param arr - array to sort
     * @param comparator - comparator
     * @param <T> - type of array elements
     */
    default <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator) {
        sort(arr, comparator, 0, arr.length);
    }

    /**
     * Sort array of primitives
     * @param arr - array of ints
     */
    default void sort(@NotNull int[] arr) {
        sort(arr, 0, arr.length);
    }


    /*  P A R T I A L   A R R A Y   S O R T */

    /**
     * Sort the part of array including {@code from} index excluding {@code to} index
     * If array contains null element, then the NullPointerException will be thrown
     *
     * Restrictions:
     * 0 <= {@code from} < array length
     * {@code from} < {@code to} <= array length
     *
     * @param arr - array to sort
     * @param <T> - comparable type
     * @param from - from index (inclusive)
     * @param to - to index (exclusive)
     */
    <T extends Comparable> void sort(@NotNull T[] arr, int from, int to);

    /**
     * Sort object array with comparator
     * Array of T may contain nulls.
     * nulls may be considered less then non-null {@link Comparator#nullsFirst(Comparator)},
     * or greater then non-null {@link Comparator#nullsLast(Comparator)}.
     *
     * Restrictions:
     * 0 <= {@code from} < array length
     * {@code from} < {@code to} <= array length
     *
     * @param arr - array to sort
     * @param comparator - comparator
     * @param <T> - type of array elements
     * @param from - from index (inclusive)
     * @param to - to index (exclusive)
     */
    <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator, int from, int to);

    /**
     * Sort array of primitives
     *
     * Restrictions:
     * 0 <= {@code from} < array length
     * {@code from} < {@code to} <= array length
     *
     * @param arr - array of ints
     * @param from - from index (inclusive)
     * @param to - to index (exclusive)
     */
    void sort(@NotNull int[] arr, int from, int to);
}