package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

class QueryFactory {
    private static final String CREATE_IF_NOT_EXIST = "create table if not exists ";
    private static final String ID_FIELD = " bigint auto_increment";
    private static final String PRIMARY_KEY = "primary key (";

    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";

    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String IF_EXIST = " IF EXISTS";

    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE_ID = "WHERE ID = ";


    /*
            For simplicity support only long indexes
     */
    @NotNull
    public static String getSelectQuery(@NotNull AnnotationManager man,
                                        @NotNull Class<?> entityClass,
                                        long key) {
        AnnotatedClass ac = man.getAnnotatedClass(entityClass);

        StringBuilder sb = new StringBuilder(SELECT).append(" ");
        sb.append(getFieldNames(man, entityClass,false)).append(" ");

        sb.append(FROM).append(" ").append(ac.getSimpleName()).append(" ");
        sb.append(WHERE_ID).append(key);
        return sb.toString().toUpperCase();
    }

    @NotNull
    public static String createTableQuery(@NotNull AnnotationManager manager,
                                          @NotNull Class<?> entityClass) {

        StringBuilder query = new StringBuilder(CREATE_IF_NOT_EXIST);


        query.append(manager.getAnnotatedClass(entityClass).getSimpleName()).append(" (");

        query.append(getFieldNames(manager, entityClass, true));

        query.append(", ").append(PRIMARY_KEY).append(manager.getId(entityClass).getName()).append("))");
        return query.toString().toUpperCase();
    }

    @NotNull
    public static String getInsertQuery(@NotNull AnnotationManager manager,
                                        @NotNull Class<?> entityClass) {

        AnnotatedClass annotatedClass = manager.getAnnotatedClass(entityClass);

        StringBuilder sb = new StringBuilder(INSERT);
        sb.append(annotatedClass.getSimpleName()).append(" (");
        sb.append(getFieldNames(manager, entityClass, false)).append(") ");
        sb.append(VALUES).append("(");

        int count = 0;
        for (AnnotatedField f: annotatedClass.getFields()) {
            if (count++ != 0)
                sb.append(",");
            if (f.contains(manager.getIdAnnotation()))
                sb.append("null");
            else
                sb.append("?");
        }

        sb.append(")");

        return sb.toString().toUpperCase();
    }

    @NotNull
    public static String getDropTableQuery(@NotNull AnnotationManager manager, Class<?> entityClass) {

        return (DROP_TABLE + manager.getAnnotatedClass(entityClass).getSimpleName() + IF_EXIST).toUpperCase();
    }

    @NotNull
    private static String getFieldNames(@NotNull AnnotationManager manager,
                                        @NotNull Class<?> entityClass,
                                        boolean isTypeInfo) {

        StringBuilder query = new StringBuilder();
        int count = 0;
        for (AnnotatedField f : manager.getAnnotatedClass(entityClass).getFields()) {
            if (count++ != 0) {
                query.append(", ");
            }
            if (isTypeInfo) {
                query.append(getFieldString(f, manager.getIdAnnotation()));
            } else {
                query.append(f.getName());
            }
        }
        return query.toString();
    }

    @NotNull
    private static String getFieldString(@NotNull AnnotatedField field, @NotNull Class<? extends Annotation> id) {

        StringBuilder sb = new StringBuilder(field.getName());

        String sqlType = sqlTypes.get(field.getType());

        if (field.contains(id)) {
            sb.append(ID_FIELD);
        } else {

            if (sqlType != null)
                sb.append(" ").append(sqlType);
            else
                throw new IllegalStateException("no supported type " + field.getType());
        }
        return sb.toString();
    }

    private static final Map<Class<?>, String> sqlTypes = new HashMap<>();

    static {
        sqlTypes.put(boolean.class, "BIT");
        sqlTypes.put(Boolean.class, "BIT");
        sqlTypes.put(byte.class, "tinyint");
        sqlTypes.put(Byte.class, "tinyint");
        sqlTypes.put(short.class, "SMALLINT");
        sqlTypes.put(Short.class, "SMALLINT");
        sqlTypes.put(char.class, "VARCHAR");
        sqlTypes.put(Character.class, "VARCHAR");
        sqlTypes.put(int.class, "INT");
        sqlTypes.put(Integer.class, "INT");
        sqlTypes.put(long.class, "BIGINT");
        sqlTypes.put(Long.class, "BIGINT");
        sqlTypes.put(float.class, "FLOAT");
        sqlTypes.put(Float.class, "FLOAT");
        sqlTypes.put(double.class, "DOUBLE");
        sqlTypes.put(Double.class, "DOUBLE");
        sqlTypes.put(String.class, "VARCHAR(256)");
    }
}