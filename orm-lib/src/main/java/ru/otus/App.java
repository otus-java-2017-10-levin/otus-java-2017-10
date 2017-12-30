package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.WrapperDataSet;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

class App {

    private final String jpa = "otusJPAH2";
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(jpa);
    private final EntityManager em = factory.createEntityManager(new PersistenceParams(jpa, "META-INF/persistence.xml").getParameters());


    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        startServer();
        WrapperDataSet primitives = WrapperDataSet.builder()
                .aBoolean(true).aByte((byte)1)
                .aChar('A').aShort((short)11)
                .anInt(100).aFloat(0.2f)
                .aLong(1000L).aDouble(3.1415)
                .build();

        System.out.println("Before persist:\n"+ primitives);
        em.persist(primitives);
        em.flush();
        System.out.println("After flush:\n"+primitives);

        WrapperDataSet fromDB = em.find(WrapperDataSet.class, primitives.getId());
        System.out.println("After find by id:\n"+fromDB);
        factory.close();

        System.out.println("EntityManagerFactory is open: " + factory.isOpen());
        System.out.println("EntityManager is open:        " + em.isOpen());
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