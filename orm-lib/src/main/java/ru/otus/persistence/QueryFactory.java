package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QueryFactory {
    private static final String CREATE_IF_NOT_EXIST = "create table if not exists ";
    private static final String ID_FIELD = "bigint";
    private static final String AUTO_INCREMENT = "auto_increment";
    private static final String PRIMARY_KEY = "primary key (";

    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";

    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String IF_EXIST = " IF EXISTS";

    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE_ID = "WHERE ID = ";

    private static final String ALTER = "alter table";
    private static final String ADD_CONSTRAINT = "add constraint";
    private static final String REFERENCES = "foreign key (%s) references";

//    private static final String ALTER_TABLE = "alter table %s add constraint %s foreign key (%s) references %s";

    /*
    one to one
create table AddressDataSet (id bigint not null, city varchar(255), street varchar(255), primary key (id))
create table UserDataSet (id bigint not null, address_id bigint, primary key (id))
alter table UserDataSet add constraint FKhqati05jw18942yayxofpgh2y foreign key (address_id) references AddressDataSet

     */

    /*
            For simplicity support only long indexes
     */
    @NotNull
    public static String getSelectQuery(@NotNull AnnotationManager man,
                                        @NotNull Class<?> entityClass,
                                        long key) {
        AnnotatedClass ac = man.getAnnotatedClass(entityClass);

        StringBuilder sb = new StringBuilder(SELECT).append(" ");
        sb.append(getFieldNames(man, entityClass, false)).append(" ");

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

        query.append(", ").append(PRIMARY_KEY);
        int counter = 0;
        AnnotatedClass annotatedClass = manager.getAnnotatedClass(entityClass);
        for (AnnotatedField af : annotatedClass.getFields(manager.getIdAnnotation())) {
            if (counter != 0)
                query.append(",");

            query.append(af.getName());
            counter++;
        }

        query.append("))");
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
        for (AnnotatedField f : annotatedClass.getFields()) {
            if (count++ != 0)
                sb.append(",");
            if (f.contains(manager.getIdAnnotation()))
                sb.append("NULL");
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
                query.append(getFieldString(manager, entityClass, f.getName()));
            } else {
                query.append(f.getName());
            }
        }
        return query.toString();
    }

    public static String getFKey(@NotNull AnnotationManager manager,
                                 @NotNull Class<?> entityClass,
                                 @NotNull String field,
                                 @NotNull Class<?> foreignClass) {
        //alter table UserDataSet add constraint FKhqati05jw18942yayxofpgh2y foreign key (address_id) references AddressDataSet

        AnnotatedField fkeyField = manager.getId(foreignClass);
        StringBuilder sb = new StringBuilder(ALTER);
        sb.append(" ").append(entityClass.getSimpleName()).append(" ");
        sb.append(ADD_CONSTRAINT).append(" ");
        String constraintName = "FK"+entityClass.getSimpleName()+foreignClass.getSimpleName()+fkeyField.getName();
        sb.append(constraintName).append(" ").append(String.format(REFERENCES, field)).append(" ");
        sb.append(foreignClass.getSimpleName());
        return sb.toString().toUpperCase();
    }

    @NotNull
    private static String getFieldString(@NotNull AnnotationManager manager,
                                         @NotNull Class<?> entityClass,
                                         @NotNull String name) {

        AnnotatedField field = manager.getField(entityClass, name);
        StringBuilder sb = new StringBuilder(field.getName());

        String sqlType = sqlTypes.get(field.getType());

        if (field.contains(manager.getIdAnnotation())) {
            sb.append(" ").append(ID_FIELD).append(" ").append(AUTO_INCREMENT);
        } else {

            if (sqlType != null)
                sb.append(" ").append(sqlType);
            else if (manager.contains(entityClass)) {
                sb.append(" ").append(ID_FIELD);
            } else
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