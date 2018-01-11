package ru.otus.persistence;

import org.dbunit.Assertion;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.otus.classes.Address;
import ru.otus.classes.UserDataSet;
import ru.otus.jdbc.H2DatabaseTest;
import ru.otus.jdbc.JdbcTestParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ALL")
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
        Address address = new Address();
        address.setAddress("111");
        address.setUser(set);
        set.setAddress(address);

        assertEquals(0, set.getId());
        em.persist(set);
        em.persist(address);
        em.close();

        checkTables("USERDATASET");
        checkTables("ADDRESS");

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
            UserDataSet user = new UserDataSet("Flow");
            Address address = new Address("100");
            user.setAddress(address);
            address.setUser(user);
            em.persist(user);
            em.persist(address);
            em.flush();

            user.setName("Flow1");
            long id = user.getId();
            user = em.find(UserDataSet.class, id);
            em.close();

            assertEquals("Flow", user.getName());
            assertEquals(id, user.getId());
            assertEquals("100", user.getAddress().getAddress());
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