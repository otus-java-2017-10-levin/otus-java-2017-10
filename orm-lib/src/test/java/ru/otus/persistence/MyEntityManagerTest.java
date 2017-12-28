package ru.otus.persistence;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UsersDataSet;
import ru.otus.jdbc.H2DatabaseTest;
import ru.otus.jdbc.JdbcTestParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MyEntityManagerTest extends H2DatabaseTest {

    private final String TABLE_NAME = "USERSDATASET";
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("otusJPAH2");
    private EntityManager em = factory.createEntityManager(JdbcTestParams.persistenceXml.getParameters());

    MyEntityManagerTest(TestInfo testInfo) {
        super(testInfo.getDisplayName());
    }

    @Test
    void persistTest() throws Exception {
        UsersDataSet set = new UsersDataSet("Flow");

        assertEquals(0, set.getId());
        em.persist(set);
        em.close();

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(TABLE_NAME);

        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable(TABLE_NAME);

        Assertion.assertEquals(expectedTable, actualTable);

        System.out.println(set.getId());

        assertEquals(true, set.getId() != 0);
    }

    @Nested
    class EntityFind {

        @Test
        void findOnClosedManager() {
            em.close();
            Exception e = assertThrows(IllegalStateException.class, () -> em.find(UsersDataSet.class, 1L));
            assertEquals(null, e.getMessage());
        }

        @Test
        void findWithWrongEntityClass() {
            Exception e = assertThrows(IllegalArgumentException.class, () -> em.find(String.class, 1L));
            assertEquals(null, e.getMessage());
        }

        @Test
        void findWithWrongIdClass() {
            Exception e = assertThrows(IllegalArgumentException.class, () -> em.find(UsersDataSet.class,1));
            assertEquals(null, e.getMessage());
        }

        @Test
        void findTest() {
            UsersDataSet set = new UsersDataSet("Flow");

            em.persist(set);
            em.flush();

            set.setName("Flow1");
            long id = set.getId();
            set = em.find(UsersDataSet.class, id);
            em.close();

            assertEquals("Flow", set.getName());
            assertEquals(id, set.getId());
        }
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/UsersDataSetPersist.xml"));
    }
}