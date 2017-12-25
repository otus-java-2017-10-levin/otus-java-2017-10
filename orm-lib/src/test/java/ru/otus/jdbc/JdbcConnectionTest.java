package ru.otus.jdbc;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.*;
import ru.otus.H2Db;

import java.sql.SQLException;


class JdbcConnectionTest extends H2DatabaseTest {

    private final String create = "insert into user (name) values ('Flow')";
    private final String createUserTable = "create table if not exists user (id bigint auto_increment, name varchar(256), primary key (id))";
    private final String TABLE_NAME = "USER";
    private DBConnection connection = DbManagerFactory.createDataBaseManager(JdbcTestParams.properties).createConnection();

    public JdbcConnectionTest(TestInfo testInfo) {
        super(testInfo.getDisplayName());
    }

    @BeforeEach
    void createTable() throws SQLException {
        H2Db.start();
        connection.execQuery(createUserTable);
    }

    @Test
    void executeQueryWithoutAnswer() throws Exception {
        assertEquals(true, connection.execQuery(create));

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(TABLE_NAME);

        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable(TABLE_NAME);

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @AfterEach
    void dropTable()  {
        connection.execQuery("drop table user");
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/expectedUsersDataSet.xml"));
    }
}