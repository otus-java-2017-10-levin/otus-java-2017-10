package ru.otus.jdbc;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.*;
import ru.otus.base.UserDataSet;

import java.sql.SQLException;


class JdbcConnectionTest extends H2DatabaseTest {

    private final String INSERT = "insert into user (name) values ('Flow')";
    private final String TABLE_NAME = "user";
    private final DBConnection connection = DbManagerFactory.createDataBaseManager(JdbcTestParams.properties).getConnection("test");

    JdbcConnectionTest(TestInfo testInfo) {
        super(testInfo.getDisplayName());
    }

    @BeforeEach
    void createTable() {
        connection.execQuery("DROP TABLE USER IF EXISTS");
        String createUserTable = "create table if not exists user (id bigint auto_increment, name varchar(256), primary key (id))";
        connection.execQuery(createUserTable);
    }

    @Test
    void testSelect() throws SQLException {
        connection.execQuery(INSERT);
        UserDataSet set = connection.execQuery("SELECT * FROM "+TABLE_NAME + " WHERE ID = 1", result -> {
            UserDataSet user = new UserDataSet();
            result.next();
            user.setId(result.getLong("id"));
            user.setName(result.getString("name"));
            return user;
        });

        assertEquals(1, set.getId());
        assertEquals("Flow", set.getName());
    }

    @Test
    void executeQueryWithoutAnswer() throws Exception {
        connection.execQuery(INSERT);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(TABLE_NAME);

        IDataSet expectedDataSet = getDataSet();
        ITable expectedTable = expectedDataSet.getTable(TABLE_NAME);

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    void executeQueryThatReturnsLong() {
        connection.execQuery(INSERT);

        try {
            long id = connection.execQuery("SELECT SCOPE_IDENTITY()", result -> {
                result.next();
                return result.getLong(1);
            });
            assertEquals(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void closeServer() {
        connection.execQuery("drop table user if exists");
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream("/expectedUsersDataSet.xml"));
    }
}