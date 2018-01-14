package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.classes.Address;
import ru.otus.classes.UserDataSet;
import ru.otus.classes.Phone;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import javax.persistence.Id;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
class QueryFactoryTest {

    private final static Class<? extends Annotation> idClass = Id.class;
    private final AnnotationManager man = new AnnotationManager(idClass, UserDataSet.class, Address.class, Phone.class);
    @Test
    void createTableFromEntity() {
        String actualQuery = QueryFactory.createTableQuery(man, UserDataSet.class);
        String expectedQuery = "CREATE TABLE IF NOT EXISTS USERDATASET (NAME VARCHAR(256), AGE INT, " +
                "ADDRESS BIGINT, ID BIGINT AUTO_INCREMENT, PRIMARY KEY (ID))";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void insertTest() {
        String actualQuery = QueryFactory.getInsertQuery(man, UserDataSet.class, true);
        String expectedQuery = "INSERT INTO USERDATASET (NAME, AGE, ADDRESS, ID) VALUES (?,?,?,?)";

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
                "USERDATASET.ADDRESS AS USERDATASET_ADDRESS, " +
                "USERDATASET.ID AS USERDATASET_ID FROM USERDATASET WHERE USERDATASET.ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getJoinQuery() {
        String actualQuery = QueryFactory.getSelectQuery(man, 1, UserDataSet.class);
        String expectedQuery = "SELECT USERDATASET.NAME AS USERDATASET_NAME, USERDATASET.AGE AS USERDATASET_AGE, " +
                "USERDATASET.ADDRESS AS USERDATASET_ADDRESS, " +
                "USERDATASET.ID AS USERDATASET_ID, ADDRESS.ADDRESS AS ADDRESS_ADDRESS, " +
                "ADDRESS.USER AS ADDRESS_USER, " +
                "ADDRESS.ID AS ADDRESS_ID " +
                "FROM USERDATASET LEFT OUTER JOIN ADDRESS ON " +
                "USERDATASET.ID = ADDRESS.USER " +
                "WHERE USERDATASET.ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void getFKeyQuery() {
        AnnotatedClass annotatedClass = man.getAnnotatedClass(UserDataSet.class);
        AnnotatedClass annotatedClass1 = man.getAnnotatedClass(Address.class);
        ConstraintImpl constraint = new ConstraintImpl(annotatedClass, annotatedClass1, "address");
        String actualQuery = QueryFactory.getFKey(constraint);
        String expectedQuery = "alter table UserDataSet add constraint FKUSERDATASETADDRESS foreign key (address) " +
                "references Address";
        assertEquals(expectedQuery.toUpperCase(), actualQuery);
    }

    @Test
    void getUpdateKeysQuery() {
        String expectedQuery = "update UserDataSet set address=? where id=?";
        String actualQuery = QueryFactory.getUpdateKeysQuery(man, UserDataSet.class);
        assertEquals(expectedQuery.toUpperCase(), actualQuery);
    }

    @Test
    void getSelectQueryWhere() {

        Class<?> entity = Phone.class;
        AnnotatedField id = man.getId(entity);
        String actualQuery = QueryFactory.getSelectQuery(man, entity, id, 1);
        String expectedQuery = "SELECT PHONE.OWNER AS PHONE_OWNER, " +
                "PHONE.PHONE AS PHONE_PHONE, " +
                "PHONE.ID AS PHONE_ID FROM PHONE WHERE PHONE.ID = 1";
        assertEquals(expectedQuery, actualQuery);
    }

    //TODO: create test selecting complex object from db via foreign key. Ex. load UserDataSets as ManyToOne
}