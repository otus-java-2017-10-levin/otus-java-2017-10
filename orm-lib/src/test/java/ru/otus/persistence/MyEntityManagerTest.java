package ru.otus.persistence;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.otus.H2Db;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.jdbc.H2DatabaseTest;
import ru.otus.jdbc.JdbcTestParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MyEntityManagerTest extends H2DatabaseTest {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("otusJPAH2");
    private final EntityManager em = factory.createEntityManager(JdbcTestParams.persistenceXml.getParameters());

    MyEntityManagerTest(TestInfo testInfo) {
        super(testInfo.getDisplayName());
    }

    private void checkTables(String table) throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(table);

        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable(table);

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void persistTest() throws Exception {
        UserDataSet set = new UserDataSet("Flow");
        PhonesDataSet phone = new PhonesDataSet();
        phone.setHouseNumber(1);
        phone.setPhone("111");
        phone.setUser(set);
        set.setPhone(phone);

        assertEquals(0, set.getId());
        em.persist(set);
        em.persist(phone);
        em.close();

        checkTables("USERDATASET");
        checkTables("PHONESDATASET");

        assertEquals(true, set.getId() != 0);
    }

    @Nested
    class EntityFind {

        @Test
        void findOnClosedManager() {
            em.close();
            Exception e = assertThrows(IllegalStateException.class, () -> em.find(UserDataSet.class, 1L));
            assertEquals(null, e.getMessage());
        }

        @Test
        void findWithWrongEntityClass() {
            Exception e = assertThrows(IllegalArgumentException.class, () -> em.find(String.class, 1L));
            assertEquals("wrong entity class", e.getMessage());
        }

        @Test
        void findWithWrongIdClass() {
            Exception e = assertThrows(IllegalArgumentException.class, () -> em.find(UserDataSet.class,1));
            assertEquals(null, e.getMessage());
        }

        @Test
        void findTest() {
            UserDataSet set = new UserDataSet("Flow");

            em.persist(set);
            em.flush();

            set.setName("Flow1");
            long id = set.getId();
            set = em.find(UserDataSet.class, id);
            em.close();

            assertEquals("Flow", set.getName());
            assertEquals(id, set.getId());
        }

        @Test
        void findTestMultipleTypes() {
            PhonesDataSet set = new PhonesDataSet("Flow");
            set.setHouseNumber(11);

            em.persist(set);
            em.flush();

            set.setPhone("Flow1");
            long id = set.getId();
            set = em.find(PhonesDataSet.class, id);
            em.close();

            assertEquals("Flow", set.getPhone());
            assertEquals(11, set.getHouseNumber());
            assertEquals(id, set.getId());
        }
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        IDataSet[] datasets = new IDataSet[] {
                new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/UsersDataSetPersist.xml")),
                new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/PhonesDataSet.xml"))
        };

        return new CompositeDataSet(datasets);
    }
}