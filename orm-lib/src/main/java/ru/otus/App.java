package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.Address;
import ru.otus.base.Phone;
import ru.otus.base.UserDataSet;
import ru.otus.dao.UserDataSetDAO;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

class App {

    private final EntityManagerFactory factory = JpaUtil.getEntityManagerFactory();

    public static void main(String[] args) throws SQLException {
        new App().run();
    }

    private UserDataSet createUser() {
        UserDataSet user = new UserDataSet("Flow");
        Address address = new Address("100");
        user.setAddress(address);
        address.setUser(user);
        user.setAge(10);
        user.addPhone(Phone.of(user, "100"));
        user.addPhone(Phone.of(user, "200"));
        user.addPhone(Phone.of(user, "300"));
        return user;
    }

    private void run() throws SQLException {
        final int cycles = 3;
        UserDataSetDAO dao = new UserDataSetDAO(factory.createEntityManager());

        startServer();

        for (int i=0; i<cycles; i++) {
            UserDataSet user = createUser();
            dao.save(user);

            UserDataSet fromDB = dao.load(user.getId());
            System.out.println(fromDB);
        }
        factory.close();
    }

    private void startServer() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    }
}