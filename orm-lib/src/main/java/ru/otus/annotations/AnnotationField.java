package ru.otus.annotations;

import javax.persistence.Id;
import java.lang.reflect.Field;

public final class AnnotationField {
    private final Field field;
    private final boolean isPrimaryKey;

    public AnnotationField(Field field) {
        if (field == null)
            throw new IllegalArgumentException();

        this.field = field;
        isPrimaryKey = field.isAnnotationPresent(Id.class);
    }

    public String getName() {
        return field.getName();
    }


    public Class<?> getType() {
        return field.getType();
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public Field getField() {
        return field;
    }
}
