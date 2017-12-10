package ru.otus.json;

import org.apache.commons.lang3.ClassUtils;
import ru.otus.common.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Collection;

public final class FieldEntity {
    private final Class<?> entityClass;
    private final Object value;

    public FieldEntity(Field field, Object object) {
        entityClass = field.getClass();
        value = ReflectionHelper.getValue(field, object);
    }

    public FieldEntity(Class<?> entityClass, Object value) {
        this.entityClass = entityClass;
        this.value = value;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Object getValue() {
        return value;
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(entityClass);
    }

    public boolean isArray() {
        return entityClass.isArray();
    }

    public boolean isPrimitive() {
        return ClassUtils.isPrimitiveOrWrapper(entityClass) || entityClass == String.class;
    }
}
