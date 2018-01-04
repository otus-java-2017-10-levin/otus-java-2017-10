package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AnnotatedField;

public class Fields {

    /**
     * Convert object to Field<?> class
     *
     * @param field  -
     * @param object -
     * @return -
     */
    @NotNull
    public static Field<?> getField(@NotNull AnnotatedField field, @NotNull Object object) throws IllegalAccessException {
        Object value = field.getFieldValue(object);

        return new SimpleFieldImpl<>(field.getName(), value, field.getType());
    }
}

