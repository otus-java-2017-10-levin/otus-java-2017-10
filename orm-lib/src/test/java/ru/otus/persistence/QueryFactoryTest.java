package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.Id;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class QueryFactoryTest {

    private final static Class<? extends Annotation> idClass = Id.class;
    private final AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class, PhonesDataSet.class);
    @Test
    void createTableFromEntity() {
        String actualQuery = QueryFactory.createTableQuery(man, UserDataSet.class);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERDATASET (NAME VARCHAR(256), AGE INT, EMPLOYEEID BIGINT, PHONE BIGINT, ID BIGINT AUTO_INCREMENT, PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void insertTest() {
        String actualQuery = QueryFactory.getInsertQuery(man, UserDataSet.class);
        String expectedQuery = "INSERT INTO USERDATASET (NAME, AGE, EMPLOYEEID, PHONE, ID) VALUES (?,?,?,?,NULL)";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void dropTables() {
        String actualQuery = QueryFactory.getDropTableQuery(man, UserDataSet.class);
        String expectedQuery = "DROP TABLE USERDATASET IF EXISTS";

        assertEquals(expectedQuery, actualQuery);

    }

    @Test
    void getSelectQuery() {
        String actualQuery = QueryFactory.getSelectQuery(man, UserDataSet.class, 1);
        String expectedQuery = "SELECT NAME, AGE, EMPLOYEEID, PHONE, ID FROM USERDATASET WHERE ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getFKeyQuery() {
        String actualQuery = QueryFactory.getFKey(man, UserDataSet.class, "phone", PhonesDataSet.class);
        String expectedQuery = "alter table UserDataSet add constraint FKUSERDATASETPHONESDATASETID foreign key (phone) references PhonesDataSet";
        assertEquals(expectedQuery.toUpperCase(), actualQuery);
    }
}
