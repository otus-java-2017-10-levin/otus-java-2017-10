package ru.otus;

import ru.otus.base.Executor;
import ru.otus.connection.DBConnection;

public class App
{
    public static void main( String[] args ) throws Exception
    {
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
