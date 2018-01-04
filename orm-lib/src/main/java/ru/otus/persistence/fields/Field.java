package ru.otus.persistence.fields;

import org.apache.commons.lang3.ClassUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public interface Field<T> {

    T getValue();

    String getName();

    Class<?> getFieldClass();

    boolean isNullValue();

    static boolean isArrayField(Class<?> cl) {
        return  cl.isArray() ||
                Collection.class.isAssignableFrom(cl);
    }

    static boolean isSimpleField(Class<?> cl) {
        Set<Class<?>> simpleClasses = new HashSet<>();
        Collections.addAll(simpleClasses, String.class, java.util.Date.class,
                           java.sql.Date.class, BigDecimal.class, BigInteger.class);


        return simpleClasses.contains(cl) || ClassUtils.isPrimitiveOrWrapper(cl);
    }
}