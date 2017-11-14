package ru.otus.internals;

import java.util.Collection;

/**
 * Class with static methods for assert values in tests
 */

public class Assert {

    public static void assertTrue(boolean value) {
        if (!value) {
            throw new RuntimeException(value + " != true");
        }
    }

    public static <T> void assertEquals(Collection<T> col1, Collection<T> col2) {
        try {
            assertTrue(col1.toString().equals(col2.toString()));
        } catch (RuntimeException e) {
            throw new RuntimeException(col1 + " != " + col2);
        }
    }

}
