package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.persistence.annotations.AnnotatedClass;

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
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERDATASET (NAME VARCHAR(256), AGE INT, " +
                "EMPLOYEEID BIGINT, PHONE BIGINT, ID BIGINT AUTO_INCREMENT, PRIMARY KEY (ID))";

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
        String expectedQuery = "SELECT USERDATASET.NAME AS USERDATASET_NAME, USERDATASET.AGE AS USERDATASET_AGE, " +
                "USERDATASET.EMPLOYEEID AS USERDATASET_EMPLOYEEID, USERDATASET.PHONE AS USERDATASET_PHONE, " +
                "USERDATASET.ID AS USERDATASET_ID FROM USERDATASET WHERE USERDATASET.ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getJoinQuery() {
        String actualQuery = QueryFactory.getSelectQuery(man, 1, UserDataSet.class);
        String expectedQuery = "SELECT USERDATASET.NAME AS USERDATASET_NAME, USERDATASET.AGE AS USERDATASET_AGE, " +
                "USERDATASET.EMPLOYEEID AS USERDATASET_EMPLOYEEID, USERDATASET.PHONE AS USERDATASET_PHONE, " +
                "USERDATASET.ID AS USERDATASET_ID, PHONESDATASET.PHONE AS PHONESDATASET_PHONE, " +
                "PHONESDATASET.HOUSENUMBER AS PHONESDATASET_HOUSENUMBER, PHONESDATASET.USER AS PHONESDATASET_USER, " +
                "PHONESDATASET.ID AS PHONESDATASET_ID " +
                "FROM USERDATASET LEFT OUTER JOIN PHONESDATASET ON " +
                "USERDATASET.ID = PHONESDATASET.USER " +
                "WHERE USERDATASET.ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getFKeyQuery() {
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);
        AnnotatedClass annotatedClass1 = man.getAnnotatedClass(PhonesDataSet.class);
        ConstraintImpl constraint = new ConstraintImpl(annotatedClass, annotatedClass1, "phone");
        String actualQuery = QueryFactory.getFKey(constraint);
        String expectedQuery = "alter table UserDataSet add constraint FKUSERDATASETPHONESDATASET foreign key (phone) " +
                "references PhonesDataSet";
        assertEquals(expectedQuery.toUpperCase(), actualQuery);
    }

    @Test
    void getUpdateKeysQuery() {
        String expectedQuery = "update UserDataSet set phone=? where id=?";
        String actualQuery = QueryFactory.getUpdateKeysQuery(man, UserDataSet.class);
        assertEquals(expectedQuery.toUpperCase(), actualQuery);
    }
}