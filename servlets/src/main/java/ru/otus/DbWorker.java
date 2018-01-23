package ru.otus;

import ru.otus.controller.dao.PhoneDAO;
import ru.otus.controller.dao.UserDAO;
import ru.otus.model.entities.Address;
import ru.otus.model.entities.Phone;
import ru.otus.model.entities.User;
import ru.otus.utils.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class DbWorker implements Runnable {

    private final EntityManagerFactory factory = JpaUtil.getEntityManagerFactory();

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

    @Override
    public void run() {
        final int cycles = 3;
        final EntityManager entityManager = factory.createEntityManager();
        UserDAO dao = new UserDAO(entityManager);

        for (int i = 0; i < cycles; i++) {
            User user = createUser();
            dao.save(user);

            User fromDB = dao.load(user.getId());
            System.out.println(fromDB);
        }

        PhoneDAO phoneDAO = new PhoneDAO(entityManager);
        while (true) {
            for (long i = 1; i < 10; i++) {
                phoneDAO.load(i);
            }
        }
    }
}
