package ru.otus.persistence.fields;

import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractField<T> implements Field<T> {
    private  T value;
    private Class<?> fieldClass;
    private String name;

    public AbstractField(@NotNull String name,
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
