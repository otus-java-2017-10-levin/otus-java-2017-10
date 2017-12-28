package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.annotations.AnnotatedClass;
import ru.otus.base.UsersDataSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class TableCreationTest {

    @Test
    void createTableFromEntity() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);
        UsersDataSet user = new UsersDataSet("Flow");

        String actualQuery = TableFactory.getQuery(annotatedClass);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERSDATASET (ID BIGINT AUTO_INCREMENT, NAME VARCHAR(256), PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void createObject() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        String actualQuery = TableFactory.getInsertQuery(annotatedClass);
        String expectedQuery = "INSERT INTO USERSDATASET (NAME) VALUES (?)";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void dropTables() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        String actualQuery = TableFactory.getDropTableQuery(annotatedClass);
        String expectedQuery = "DROP TABLE USERSDATASET IF EXISTS";

        assertEquals(expectedQuery, actualQuery);
    }
}
