package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.connection.MessageConnection;
import ru.orus.connection.MessageSession;
import ru.orus.messages.Header;
import ru.orus.messages.Messages;
import ru.otus.db.entities.Address;
import ru.otus.db.entities.DataSet;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;
import ru.otus.utils.GsonUtil;
import ru.otus.utils.MSUtil;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

@SuppressWarnings("UnusedReturnValue")
public class DbWorker extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DbWorker.class);

    private MessageSession session;

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
            MessageConnection localhost = MSUtil.getConnection();
            session = localhost.getSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.declareTopic("basic");
    }

    @Override
    public void run() {
        final int cycles = 3;

        for (int i = 0; i < cycles; i++) {
            System.out.println("creating user");
            User user = createUser();
            try {
                save(user);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                load(1, User.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <T extends DataSet> T load(long id, Class<T> cl) throws InterruptedException {
        final Header head = new Messages.BasicProperties.Builder()
                .topic("basic")
                .replyTo("basic-reply")
                .id(Messages.getRandomId())
                .addAttribute("action", "load")
                .addAttribute("class", User.class.getCanonicalName())
                .build();

        session.sendMessage(""+id, head);

        final ArrayBlockingQueue<T> response = new ArrayBlockingQueue<>(1);
        session.addFilter("basic-reply", message -> {
            log.info("incoming message: " + message);
            if (head.getId().equals(message.getId())) {
                try {
                    response.add(GsonUtil.fromJson(message.getBody(), cl));

                } catch (NumberFormatException e) {
                    log.error("", e);
                }
            }
        });

        return response.take();
    }

    private void save(User user) throws InterruptedException {

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
            log.info("incoming message: " + message);
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