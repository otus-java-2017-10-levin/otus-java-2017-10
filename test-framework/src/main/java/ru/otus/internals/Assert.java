package ru.otus.internals;

import java.util.List;

/**
 * Class with static methods for assert values in tests
 */

public class Assert {

    public static void assertTrue(boolean value) throws RuntimeException {
        if (!value) {
            throw new RuntimeException();
        }
    }

    public static <T> void assertEquals(List<T> col1, List<T> col2) throws RuntimeException {
        assertTrue(col1!= null && col2!=null);
        assertTrue(col1.size() == col2.size());

        for (int i=0; i < col1.size(); i++) {
            if (!col1.get(i).equals(col2.get(i))) {
                throw new RuntimeException();
            }
        }
    }
}
