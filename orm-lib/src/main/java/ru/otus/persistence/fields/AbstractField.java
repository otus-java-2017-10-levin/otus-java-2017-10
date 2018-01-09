package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractField<T> implements Field<T> {
    private final T value;
    private final Class<?> fieldClass;
    private final String name;

    AbstractField(@NotNull String name,
                  @Nullable T value,
                  @NotNull Class<?> fieldClass) {
        this.name = name;
        this.value = value;
        this.fieldClass = fieldClass;
    }

    @Override
    @Nullable
    public T getValue() {
        return this.value;
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    @NotNull
    public Class<?> getFieldClass() {
        return this.fieldClass;
    }

    @Override
    public boolean isNullValue() {
        if (fieldClass.isPrimitive()) {
            if (fieldClass.equals(long.class)) {
                return (Long)value == 0;
            }
        }

        return value==null;
    }
}
