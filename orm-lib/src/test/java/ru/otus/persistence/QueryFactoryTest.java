package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UserDataSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class QueryFactoryTest {

    @Test
    void createTableFromEntity() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        String actualQuery = QueryFactory.createTableQuery(annotatedClass);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERDATASET (NAME VARCHAR(256), AGE INT, ID BIGINT AUTO_INCREMENT, PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void insertTest() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        String actualQuery = QueryFactory.getInsertQuery(annotatedClass);
        String expectedQuery = "INSERT INTO USERDATASET (NAME, AGE, ID) VALUES (?,?,NULL)";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void dropTables() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        String actualQuery = QueryFactory.getDropTableQuery(annotatedClass);
        String expectedQuery = "DROP TABLE USERDATASET IF EXISTS";

        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    void getSelectQuery() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        String actualQuery = QueryFactory.getSelectQuery(annotatedClass, 1);
        String expectedQuery = "SELECT NAME, AGE, ID FROM USERDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getSelectMultipleQuery() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(PhoneDataSet.class);

        String actualQuery = QueryFactory.getSelectQuery(annotatedClass, 1);
        String expectedQuery = "SELECT PHONE, HOUSENUMBER, ID FROM PHONEDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }
}
