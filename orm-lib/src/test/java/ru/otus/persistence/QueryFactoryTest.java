package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UserDataSet;

import javax.persistence.Id;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class QueryFactoryTest {

    private final static Class<? extends Annotation> idClass = Id.class;

    @Test
    void createTableFromEntity() {
        AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class);
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);

        String actualQuery = QueryFactory.createTableQuery(annotatedClass, idClass);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERDATASET (NAME VARCHAR(256), AGE INT, ID BIGINT AUTO_INCREMENT, PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void insertTest() {
        AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class);
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);

        String actualQuery = QueryFactory.getInsertQuery(annotatedClass, idClass);
        String expectedQuery = "INSERT INTO USERDATASET (NAME, AGE, ID) VALUES (?,?,NULL)";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void dropTables() {
        AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class);
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);

        String actualQuery = QueryFactory.getDropTableQuery(annotatedClass);
        String expectedQuery = "DROP TABLE USERDATASET IF EXISTS";

        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    void getSelectQuery() {
        AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class);
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);

        String actualQuery = QueryFactory.getSelectQuery(annotatedClass, Id.class, 1);
        String expectedQuery = "SELECT NAME, AGE, ID FROM USERDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }
}
