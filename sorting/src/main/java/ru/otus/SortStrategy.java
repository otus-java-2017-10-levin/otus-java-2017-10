package ru.otus;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Contains two main methods for sorting comparable objects, and primitive integers.
 * Sorting only ints is for simplicity.
 */
public interface SortStrategy {

    /**
     * Sort array of objects.
     * If array contains null element, then the NullPointerException will be thrown
     * @param arr - array to sort
     * @param <T> - comparable type
     */
    <T extends Comparable> void sort(@NotNull T[] arr);

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
    <T> void sort(@NotNull T[] arr, @NotNull Comparator<T> comparator);

    /**
     * Sort array of primitives
     * @param arr - array of ints
     */
    void sort(@NotNull int[] arr);
}