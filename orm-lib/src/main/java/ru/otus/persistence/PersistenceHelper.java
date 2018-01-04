package ru.otus.persistence;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.jdbc.DBConnection;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

final class PersistenceHelper {

    /**
     * Returns a phone of setter for field named {@code id} or throws error
     * Checks if the {@code cl} contains setter method for {@code fieldName}
     *
     * @param cl          -
     * @param fieldName   -
     * @param checkSetter - if true checks setter
     * @return -
     */
    @NotNull
    public static String setterFromField(@NotNull Class<?> cl,
                                         @NotNull String fieldName,
                                         boolean checkSetter) {
        String setter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        if (checkSetter) {
            if (hasMethod(cl, setter)) {
                return setter;
            } else
                throw new IllegalArgumentException("No setter for field " + fieldName);
        }

        return setter;
    }

    public static void setFieldValue(@NotNull Object obj,
                                     @NotNull AnnotatedField field,
                                     @NotNull Object value) throws NoSuchMethodException {

        String name = field.getName();
        String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

        try {
            Class<?> type = field.getType();
            if (type == char.class || type == Character.class) {
                Character c = ((String) value).charAt(0);
                MethodUtils.invokeMethod(obj, setter, c);
            } else if (type == float.class || type == Float.class) {
                Double newVal = (Double) value;
                MethodUtils.invokeMethod(obj, setter, newVal.floatValue());
            } else
                MethodUtils.invokeMethod(obj, setter, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static boolean hasMethod(@NotNull Class<?> cl,
                                     @NotNull String method) {
        return Arrays.stream(cl.getMethods()).anyMatch(m -> m.getName().equals(method));
    }

    @Nullable
    public static <T> T find(@NotNull DBConnection connection,
                             @NotNull AnnotationManager annotationManager,
                             @NotNull Class<T> entityClass, long id) {
        T cls = null;
        try {
            cls = connection.execQuery(QueryFactory.getSelectQuery(annotationManager, entityClass, id), result -> {

                ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
                ResultSetMetaData rsmd = result.getMetaData();
                result.next();

                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String name = rsmd.getColumnName(i);
                    builder.set(annotationManager.getField(entityClass, name), result.getObject(i));
                }
                return builder.build();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cls;
    }
}