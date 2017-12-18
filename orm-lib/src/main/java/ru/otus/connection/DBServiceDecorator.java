package ru.otus.connection;

import ru.otus.base.DBService;
import ru.otus.connection.DBConnection;

import java.sql.SQLException;
import java.util.List;

public class DBServiceDecorator extends DBConnection {

    private final DBService service;
    public DBServiceDecorator(DBService service) {
        super();
        this.service = service;
    }

    @Override
    public String getMetaData() {
        return service.getMetaData();
    }

    @Override
    public void prepareTables() throws SQLException {
        service.prepareTables();
    }

    @Override
    public void addUsers(String... names) throws SQLException {
        System.out.print("Adding users. ");
        long start = System.nanoTime();
        service.addUsers(names);
        System.out.println((System.nanoTime() - start)/ 1_000_000 + " ms.");
    }

    @Override
    public String getUserName(int id) throws SQLException {
        System.out.print("Get user name. ");
        long start = System.nanoTime();
        String userName = service.getUserName(id);
        System.out.println((System.nanoTime() - start)/ 1_000_000 + " ms.");
        return userName;
    }

    @Override
    public List<String> getAllNames() throws SQLException {
        System.out.print("get all users. ");
        long start = System.nanoTime();
        List<String> allNames = service.getAllNames();
        System.out.println((System.nanoTime() - start)/ 1_000_000 + " ms.");
        return allNames;
    }

    @Override
    public void deleteTables() throws SQLException {
        service.deleteTables();
    }

    @Override
    public void close() throws Exception {
        service.close();
    }
}
