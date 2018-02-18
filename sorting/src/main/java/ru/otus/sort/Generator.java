package ru.otus.sort;

import java.util.Random;
import java.util.stream.Stream;

/**
 * Static class for generating random sequences.
 * Generates three types of sequences:
 * <ul>
 *     <li>1. Random.</li>
 *     <li>2. Nearly sorted.</li>
 *     <li>3. Reversed.</li>
 * </ul>
 */

@SuppressWarnings("ALL")
public final class Generator {

    /**
     * Generate ascending array with some permutations
     * Maximum delta of permutation is delta/2.

     * @param size - size of array
     * @param delta - permutation delta
     * @return - generated array
     */
    public static Integer[] generateNearlySorted(final int size,
                                                 final int delta) {
        if (size < 1)
            throw new IllegalArgumentException();

        Integer[] integers = new Integer[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int shift = random.nextInt(delta) - delta / 2;
            integers[i] = i + shift;
        }
        return integers;
    }

    /**
     * Generates descending array
     * @param size - size of the array
     * @return - generated array
     */
    public static Integer[] generateReversed(int size) {
        if (size < 0)
            throw new IllegalArgumentException();

        return Stream.iterate(size, integer -> integer > 0, integer -> integer-1).toArray(Integer[]::new);
    }

    /**
     * Generates array of {@code size} filled with values in {@code [0, max]}.
     * @param size - size of array
     * @param max - max element of the array
     * @return - generated array
     */
    public static Integer[] generateRandom(int size, int max) {
        if (size < 0 || max < 1)
            throw new IllegalArgumentException();

        Random random = new Random();
        return random.ints(0, max, size).boxed().toArray(Integer[]::new);
    }
}
