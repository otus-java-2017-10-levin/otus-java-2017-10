package ru.otus;

import ru.otus.db.dao.PhoneDAO;
import ru.otus.db.dao.UserDAO;
import ru.otus.db.entities.Address;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;
import ru.otus.utils.JpaUtil;

import javax.persistence.EntityManager;

public class DbWorker implements Runnable {

    private final JpaUtil jpa;
    private User createUser() {
        User user = new User("Flow");
        Address address = new Address("100");
        user.setAddress(address);
        address.setUser(user);
        user.setAge(10);
        user.addPhone(Phone.of(user, "100"));
        user.addPhone(Phone.of(user, "200"));
        user.addPhone(Phone.of(user, "300"));
        return user;
    }

    public DbWorker(JpaUtil jpa) {
        this.jpa = jpa;
    }

    @Override
    public void run() {
        final int cycles = 3;

        final EntityManager entityManager = jpa.getFactory().createEntityManager();

        UserDAO dao = new UserDAO(entityManager);

        for (int i = 0; i < cycles; i++) {
            User user = createUser();
            dao.save(user);

            User fromDB = dao.load(user.getId());
            System.out.println(fromDB);
        }

        PhoneDAO phoneDAO = new PhoneDAO(entityManager);
        while (!Thread.currentThread().isInterrupted()) {
            for (long i = 1; i < 10; i++) {
                phoneDAO.load(i);
            }
        }
    }
}