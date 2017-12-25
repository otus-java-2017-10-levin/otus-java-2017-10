package ru.otus.persistence;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.otus.base.UsersDataSet;
import ru.otus.jdbc.H2DatabaseTest;
import ru.otus.jdbc.JdbcTestParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class MyEntityManagerTest extends H2DatabaseTest {

    private final String TABLE_NAME = "USERSDATASET";

    public MyEntityManagerTest(TestInfo testInfo) {
        super(testInfo.getDisplayName());
    }

    @Test
    void persistTest() throws Exception {
        UsersDataSet set = new UsersDataSet(1, "Flow");
        UsersDataSet set1 = new UsersDataSet(3, "Flow1");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("otusJPAH2");
        EntityManager em = factory.createEntityManager(JdbcTestParams.persistenceXml.getParameters());
        em.persist(set);
        em.persist(set1);
        em.close();

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(TABLE_NAME);

        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable(TABLE_NAME);

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/UsersDataSetPersist.xml"));
    }
}