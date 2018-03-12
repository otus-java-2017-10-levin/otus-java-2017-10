package ru.otus.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.db.dao.GenericDAO;
import ru.otus.db.entities.Address;
import ru.otus.db.entities.DataSet;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Function;

import static ru.otus.db.dao.GenericDAO.of;

public class DbService {

    private SessionFactory sessionFactory;


    public DbService() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(DataSet.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Phone.class);
        configuration.addAnnotatedClass(Address.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "Flow");
        configuration.setProperty("hibernate.connection.password", "grandmaster");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "false");

        sessionFactory = createSessionFactory(configuration);
    }

    public <T extends DataSet> void save(T dataSet, Class<T> cl) {
        try (Session session = sessionFactory.openSession()) {
            final GenericDAO<T> dao = GenericDAO.of(cl, session);
            dao.save(dataSet);
        }
    }

    public <R extends DataSet> R read(long id, Class<R> type) {
        return runInSession(session -> GenericDAO.of(type, session).load(id));
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}