package ru.otus.connection;

import ru.otus.base.DBService;
import ru.otus.base.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection implements DBService {

    private final Connection connection;
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint auto_increment, name varchar(256), primary key (id))";
    private static final String INSERT_USER = "insert into user (name) values (?)";
    private static final String DELETE_USER = "drop table user";
    private static final String SELECT_USER = "select name from user where id=%s";
    private static final String GET_ALL_USERS = "select name from user";


    public DBConnection() {
        connection = ConnectionHelper.getConnection();
    }


    public void close() throws Exception {
        connection.close();
    }

    @Override
    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public void prepareTables() throws SQLException {
        Executor executor = new Executor(connection);
        executor.execQuery(CREATE_TABLE_USER);
    }

    @Override
    public void addUsers(String... names) throws SQLException {
        Executor executor = new Executor(connection);
        executor.execUpdate(INSERT_USER, statement -> {
            for (String name : names) {
                statement.setString(1, name);
                statement.execute();
            }
        });
    }

    @Override
    public String getUserName(int id) throws SQLException {
        Executor executor = new Executor(getConnection());
        return executor.execQuery(String.format(SELECT_USER, id), set -> {
            set.next();
            return set.getString("name");
        });
    }

    @Override
    public List<String> getAllNames() throws SQLException {
        Executor executor = new Executor(getConnection());
        return executor.execQuery(GET_ALL_USERS, set -> {
            ArrayList<String> objects = new ArrayList<>();
            do {
                set.next();
                objects.add(set.getString("name"));
            } while (!set.isLast());
            return objects;
        });
    }

    @Override
    public void deleteTables() throws SQLException {
        Executor executor = new Executor(connection);
        executor.execQuery(DELETE_USER);
    }

    protected Connection getConnection() {
        return connection;
    }
}
