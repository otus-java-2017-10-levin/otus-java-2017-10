package ru.otus.persistence;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PersistenceHelper {

    /**
     * Returns a name of setter for field named {@code id} or throws error
     * Checks if the {@code cl} contains setter method for {@code fieldName}
     * @param cl -
     * @param fieldName -
     * @param checkSetter - if true checks setter
     * @return -
     */
    public static String setterFromField(Class<?> cl, String fieldName, boolean checkSetter) {
        if (fieldName == null || cl == null)
            throw new IllegalArgumentException();

        String setter = "get" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);

        if (checkSetter) {
            if (hasMethod(cl, setter)) {
                return setter;
            } else
                throw new IllegalArgumentException("No setter for field " + fieldName);
        }

        return setter;
    }

    private static boolean hasMethod(Class<?> cl, String method) {
        return Arrays.stream(cl.getMethods()).anyMatch(m -> m.getName().equals(method));
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchMethodException {
        String setter = "set" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);

        try {
            MethodUtils.invokeMethod(obj, setter, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
