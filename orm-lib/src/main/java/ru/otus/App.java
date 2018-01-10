package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.dao.UserDataSetDAO;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

class App {

    private final String jpa = "otusJPAH2";
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(jpa);
    private final PersistenceParams params = new PersistenceParams(jpa, "META-INF/persistence.xml");

    public static void main(String[] args) throws SQLException {
        new App().run();
    }

    private void run() throws SQLException {

        startServer();
        UserDataSet user = new UserDataSet("Flow");
        PhonesDataSet phone = new PhonesDataSet("100", 1);
        user.setPhone(phone);
        phone.setUser(user);
        user.setAge(10);

        UserDataSetDAO dao = new UserDataSetDAO(factory.createEntityManager(params.getParameters()));
        dao.save(user);

      //  UserDataSet fromDB = dao.load(user.getId());
        //System.out.println("After find by id:\n" + fromDB);
        factory.close();

    }

    private void startServer() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

    }
}