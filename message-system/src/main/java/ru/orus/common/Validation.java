package ru.orus.common;

public final class Validation {

    public static void validateNonNull(String errorMessage, Object... vars) {
        for (Object v: vars)
            if (v == null)
                throw new IllegalArgumentException(errorMessage);
    }
}
