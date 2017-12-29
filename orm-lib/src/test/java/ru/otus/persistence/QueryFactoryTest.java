package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.annotations.AnnotatedClass;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UsersDataSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class QueryFactoryTest {

    @Test
    void createTableFromEntity() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);
        UsersDataSet user = new UsersDataSet("Flow");

        String actualQuery = QueryFactory.createTableQuery(annotatedClass);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERSDATASET (ID BIGINT AUTO_INCREMENT, NAME VARCHAR(256), PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void insertTest() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        String actualQuery = QueryFactory.getInsertQuery(annotatedClass);
        String expectedQuery = "INSERT INTO USERSDATASET (ID, NAME) VALUES (?,?)";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void dropTables() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        String actualQuery = QueryFactory.getDropTableQuery(annotatedClass);
        String expectedQuery = "DROP TABLE USERSDATASET IF EXISTS";

        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    void getSelectQuery() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        String actualQuery = QueryFactory.getSelectQuery(annotatedClass, 1);
        String expectedQuery = "SELECT (ID, NAME) FROM USERSDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getSelectMultipleQuery() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(PhoneDataSet.class);

        String actualQuery = QueryFactory.getSelectQuery(annotatedClass, 1);
        String expectedQuery = "SELECT (ID, PHONE, HOUSENUMBER) FROM PHONEDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }
}
