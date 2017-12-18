package ru.otus.orm;

import lombok.val;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts Class to OrmRepresentation
 * <p>
 * Orm annotation verification:
 * Class passed to be parsed by orm must specify several annotation.
 * 1. Before class we must use:
 * - @Table annotation
 * - name attribute
 * - uniqueConstraints array
 * <p>
 * 2. Before each method we want to represent a column in generated table
 * - @Id for primary key. If @GeneratedValud is not set use:
 * {@code @GeneratedValue(strategy = GenerationType.IDENTITY)}
 * <p>
 * - @Column for each method. If name attribute is not set. Use generated one.
 */


public final class OrmClassParser {

    private boolean isIdSet = false;
    public OrmClassParser() {
    }


    /**
     * Converts class to OrmRepresentation.
     * Checks if annotation is valid, if not - throw an exception
     *
     * @param cl - class to be converted
     * @return - OrmRepresentation object.
     * @throws IllegalArgumentException - if error in annotations
     */

    /* нужно делать для полей и для методов отдельный парсинг. НО! аннотации и для полей и метолов одинаковые.
       выносим логику разбора аннотаций в отдельный класс. Реализуем что-то типа стратегии
       функция convert делится на 3 части.
       1. разбор полей
       2. разбор методов.
       3. Разбор аннотаций класса

       Если встречаются колизии имен столбцов (@Column (name)) то выдаем ошибку.
    */
    public OrmRepresentation convert(Class<?> cl) throws IllegalArgumentException {
        val classAnno = getAnnotations(cl);

        checkClassAnnotations(classAnno);

        OrmRepresentation representation = new OrmRepresentation(cl);

        saveClassInfo(cl, (Table)classAnno.get(Table.class), representation);

        //        for (Field field : fields) {
//            saveField(field, representation);
//        }
        for (Method m: cl.getMethods()) {
            saveMethod(m, representation);
        }

        return representation;
    }

    private void saveMethod(Method m, OrmRepresentation representation) {
        if (m == null || representation == null)
            throw new IllegalArgumentException();

        val fieldAnnotations = getAnnotations(m);
        if (fieldAnnotations.containsKey(Id.class)) {
            if (isIdSet)
                throw new IllegalArgumentException("Id field duplicate");

            AnnotationProcessor processor = new AnnotationProcessorHandler();
            OrmField f = processor.processId(m.getDeclaredAnnotations());
            f.setReturnType(m.getGenericReturnType().getClass());
            f.setMethodName(m.getName());
            representation.add(f);
            isIdSet = true;
        }
    }

    private void saveClassInfo(Class<?> cl, Table anno, OrmRepresentation representation) {
        if (anno == null || representation == null)
            throw new IllegalArgumentException();

        String tableName = anno.name();

        if (tableName.equals("")) {
            tableName = cl.getSimpleName();
        }

        representation.setTableName(tableName);
    }


    private void checkClassAnnotations(Map<Class<? extends Annotation>, Annotation> annotationMap) {
        if (annotationMap.size() == 0)
            throw new IllegalArgumentException("no class annotation found!");

        if (!annotationMap.containsKey(Table.class)) {
            throw new IllegalArgumentException("no @table annotation found");
        }
    }

    private Map<Class<? extends Annotation>, Annotation> getAnnotations(Method method) {
        return mapAnnotations(method.getAnnotations());
    }

    private Map<Class<? extends Annotation>, Annotation> getAnnotations(Class<?> cl) {
        return mapAnnotations(cl.getAnnotations());
    }

    private Map<Class<? extends Annotation>, Annotation> mapAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations).collect(Collectors.toMap(Annotation::annotationType, annotation -> annotation));
    }
}