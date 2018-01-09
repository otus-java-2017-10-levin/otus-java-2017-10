package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ArrayFieldImpl<T> implements ArrayField<T> {
    private final String name;
    private final T[] value;
    private final Class fieldClass;

    @Override
    public void add(T t) {
    }

    ArrayFieldImpl(@NotNull String name,
                   @NotNull T[] object) {

        this.name = name;
        this.value = object;
        this.fieldClass = this.value.getClass();
    }

    @Override
    @Nullable
    public T[] getArray() {
       return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public Class getFieldClass() {
        return fieldClass;
    }
}
