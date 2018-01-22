package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.Address;
import ru.otus.base.Phone;
import ru.otus.base.UserDataSet;
import ru.otus.dao.PhoneDAO;
import ru.otus.dao.UserDataSetDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

class App {

    private final EntityManagerFactory factory = JpaUtil.getEntityManagerFactory();

    public static void main(String[] args) throws SQLException, InterruptedException {
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

    private void run() throws SQLException, InterruptedException {
        final int cycles = 3;
        final EntityManager entityManager = factory.createEntityManager();
        UserDataSetDAO dao = new UserDataSetDAO(entityManager);

        startServer();

        for (int i = 0; i < cycles; i++) {
            UserDataSet user = createUser();
            dao.save(user);

            UserDataSet fromDB = dao.load(user.getId());
            System.out.println(fromDB);
        }


        PhoneDAO phoneDAO = new PhoneDAO(entityManager);
        long totalLoads = 0;
        long num = 0;
        while (true) {
            for (long i = 1; i < 10; i++) {
                long start = System.nanoTime();
                Phone phone = phoneDAO.load(i);
                totalLoads += System.nanoTime() - start;
                num++;
                System.out.println(phone);
                Thread.sleep(333);
            }
            System.out.println((double)totalLoads/ num / 1_000_000 + "ms per load");
        }
//        factory.close();
    }

    private void startServer() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    }
}