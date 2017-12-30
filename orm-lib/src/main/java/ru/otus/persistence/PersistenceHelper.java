package ru.otus.persistence;

import org.apache.commons.lang3.reflect.MethodUtils;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

final class PersistenceHelper {

    /**
     * Returns a phone of setter for field named {@code id} or throws error
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

    public static void setFieldValue(Object obj, AnnotatedField field, Object value) throws NoSuchMethodException {
        String name = field.getName();
        String setter = "set" + name.substring(0, 1).toUpperCase()+name.substring(1);

        try {
            Class<?> type = field.getType();
            if (type == char.class || type == Character.class ) {
                Character c = ((String)value).charAt(0);
                MethodUtils.invokeMethod(obj, setter, c);
            } else if (type == float.class || type == Float.class) {
                Double newVal = (Double)value;
                MethodUtils.invokeMethod(obj, setter, newVal.floatValue());
            } else
                MethodUtils.invokeMethod(obj, setter, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
