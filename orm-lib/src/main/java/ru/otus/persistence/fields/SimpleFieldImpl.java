package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleFieldImpl<T> extends AbstractField<T> implements SimpleField<T> {
    SimpleFieldImpl(@NotNull String name,
                           @Nullable T value,
                           @NotNull Class<?> fieldClass) {
        super(name, value, fieldClass);
    }
}