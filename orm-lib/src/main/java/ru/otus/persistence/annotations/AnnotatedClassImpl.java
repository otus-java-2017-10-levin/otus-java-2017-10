package ru.otus.persistence.annotations;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.Constraint;

import javax.persistence.OneToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

class AnnotatedClassImpl implements AnnotatedClass {

    private static Map<Class<?>, AnnotatedClass> cacheClasses;
    private final Class<?> annotatedClass;
    private final NameGenerator generator;
    private final Map<String, AnnotatedField> fields = new LinkedHashMap<>();
    private final List<Constraint> constraints = new ArrayList<>();


    @NotNull
    @Override
    public Class<?> getAnnotatedClass() {
        return annotatedClass;
    }

    private AnnotatedClassImpl(Class<?> cl, NameGenerator nameGenerator) {
        this.annotatedClass = cl;
        generator = nameGenerator;
        parse();
    }

    /**
     * Create AnnotatedClassImpl instance.
     *
     * @param key - class to create AnnotatedClassImpl instance
     * @return - instance of AnnotatedClassImpl
     * @throws IllegalArgumentException - if class has no @Id field
     */
    public static <T> AnnotatedClass of(Class<T> key) {
        if (cacheClasses == null)
            cacheClasses = new HashMap<>();

        if (cacheClasses.containsKey(key)) {
            return cacheClasses.get(key);
        } else {
            AnnotatedClass cl = new AnnotatedClassImpl(key, String::toUpperCase);
            cacheClasses.put(key, cl);
            return cl;
        }
    }

    @Override
    public String toString() {
        return "AnnotatedClassImpl{" +
                "annotatedClass=" + annotatedClass +
                ", generator=" + generator +
                ", fields=" + fields +
                '}';
    }



    @NotNull
    @Override
    public List<AnnotatedField> getFields() {
        return new ArrayList<>(fields.values());
    }

    @Override
    public boolean is(@NotNull Object entity) {
        return annotatedClass.equals(entity.getClass());
    }

    @NotNull
    @Override
    public String getSimpleName() {
        return annotatedClass.getSimpleName();
    }

    @NotNull
    @Override
    public AnnotatedField getField(@NotNull String fieldName) {
        String name = generator.generate(fieldName);
        return fields.get(name);
    }

    @NotNull
    @Override
    public List<AnnotatedField> getFields(@NotNull Class<? extends Annotation> annotation) {
        return fields.values().stream().filter(field -> field.contains(annotation)).collect(Collectors.toList());
    }


    private void parse() {
        for (Field f : FieldUtils.getAllFields(annotatedClass)) {
            AnnotatedField af = new AnnotatedFieldImpl(f);
            fields.put(generator.generate(f.getName()), af);
        }
    }
}
