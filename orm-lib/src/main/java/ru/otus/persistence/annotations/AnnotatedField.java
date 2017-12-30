package ru.otus.persistence.annotations;

import javax.persistence.Id;
import java.lang.reflect.Field;

public final class AnnotatedField {
    private final Field field;
    private final boolean isPrimaryKey;

    public AnnotatedField(Field field) {
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
