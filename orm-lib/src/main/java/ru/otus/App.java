package ru.otus;

import ru.otus.base.UsersDataSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;

public class App
{
    public static void main( String[] args ) throws Exception
    {

        UsersDataSet set = new UsersDataSet(1, "Flow");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("otusJPAPSQL", new HashMap());
        EntityManager em = factory.createEntityManager();

        em.persist(set);

        em.close();

//        try (DBConnection connection = new DBServiceDecorator(new DBConnection())) {
//            System.out.println(connection.getMetaData());
//            connection.prepareTables();
//            connection.addUsers("Flow", "Jolly");
//            System.out.println("get the first user phone: " + connection.getUserName(1));
//            System.out.println(connection.getAllNames());
//            connection.deleteTables();
//        }
    }
}
