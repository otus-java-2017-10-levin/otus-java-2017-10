package ru.otus.orm;

import lombok.*;

import java.lang.reflect.ParameterizedType;

/**
 * Represents orm field:
 * field name, field type, getter method
 */

@Setter
@Getter
@EqualsAndHashCode(exclude={"returnType", "methodName", "isPrimaryKey"})
@Builder
class OrmField {
    private OrmField(String name, boolean isPrimaryKey) {
        this.name = name;
        this.isPrimaryKey = isPrimaryKey;
    }

    private final String name;
    private Class<?> returnType;
    private String methodName;
    private final boolean isPrimaryKey;
}
