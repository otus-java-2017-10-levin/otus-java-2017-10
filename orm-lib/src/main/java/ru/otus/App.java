package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.UserDataSet;
import ru.otus.dao.UserDataSetDAO;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

class App {

    private final String jpa = "otusJPAH2";
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(jpa);
    private final PersistenceParams params = new PersistenceParams(jpa, "META-INF/persistence.xml");


    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        startServer();
        UserDataSet user = new UserDataSet("Flow");
        user.setAge(10);

        UserDataSetDAO dao = new UserDataSetDAO(factory.createEntityManager(params.getParameters()));
        dao.save(user);
        System.out.println("After flush:\n"+user);

        UserDataSet fromDB = dao.load(user.getId());
        System.out.println("After find by id:\n"+fromDB);
        factory.close();

        System.exit(0);
    }

    private void startServer() {
        try {
            Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}