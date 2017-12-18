package ru.otus.orm;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Interface for parsing JPA annotation
 */
public interface AnnotationProcessor {

    /**
     * Parse annotation for {@code }@Id} annotation
     * We can mark field or method with this annotation.
     * @param annotations - array of annotation
     * @return OrmField
     * @throws IllegalArgumentException - if annotations is null
     */
    OrmField processId(Annotation[] annotations) throws IllegalArgumentException;

    /**
     * Parse annotation for {@code Column} annotation
     * @param annotations - array of annotation
     * @return OrmField
     * @throws IllegalArgumentException - if annotations is null
     */
    OrmField processColumn(Annotation[] annotations) throws IllegalArgumentException;
}
