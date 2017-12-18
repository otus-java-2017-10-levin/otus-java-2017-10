package ru.otus;

import ru.otus.base.UsersDataSet;
import ru.otus.connection.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App
{
    public static void main( String[] args ) throws Exception
    {

        UsersDataSet set = new UsersDataSet(1, "Flow");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PetShop");
        EntityManager em = factory.createEntityManager();

        em.persist(set);

        em.close();

        try (DBConnection connection = new DBServiceDecorator(new DBConnection())) {
            System.out.println(connection.getMetaData());
            connection.prepareTables();
            connection.addUsers("Flow", "Jolly");
            System.out.println("get the first user name: " + connection.getUserName(1));
            System.out.println(connection.getAllNames());
            connection.deleteTables();
        }

        System.out.println( "Hello World!" );
    }
}
