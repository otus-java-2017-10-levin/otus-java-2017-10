package ru.otus;

import ru.orus.connection.MessageConnection;
import ru.orus.connection.MessageSession;
import ru.orus.messages.Header;
import ru.orus.messages.Messages;
import ru.otus.db.dao.PhoneDAO;
import ru.otus.db.dao.UserDAO;
import ru.otus.db.entities.Address;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;
import ru.otus.utils.GsonUtil;
import ru.otus.utils.JpaUtil;
import ru.otus.utils.MSUtil;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class DbWorker implements Runnable {

    private MessageSession session;
    private MessageConnection localhost;
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

    public DbWorker() {
        try {
            localhost = MSUtil.getConnection();
            session = localhost.getSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final int cycles = 3;

//        for (int i = 0; i < cycles; i++) {
            User user = createUser();
            try {
                save(user);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            User fromDB = load(user.getId());
//            System.out.println(fromDB);
//        }
    }

    private User load(long id) {
        return null;
    }

    private void save(User user) throws InterruptedException {
        session.declareTopic("basic");

        final Header head = new Messages.BasicProperties.Builder()
                .topic("basic")
                .replyTo("basic-reply")
                .id(Messages.getRandomId())
                .addAttribute("action", "save")
                .addAttribute("class", User.class.getCanonicalName())
                .build();

        session.sendMessage(GsonUtil.toJson(user), head);

        final ArrayBlockingQueue<Long> response = new ArrayBlockingQueue<>(1);
        session.addFilter("basic-reply", message -> {
            if (head.getId().equals(message.getId())) {

                try {
                    response.add(Long.parseLong(message.getBody()));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        user.setId(response.take());
    }
}