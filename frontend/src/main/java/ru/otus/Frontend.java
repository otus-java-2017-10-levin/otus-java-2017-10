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
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

public class Frontend {

    private static final Logger log = LoggerFactory.getLogger(Frontend.class);

    private MessageSession session;
    private static final String basic = "basic";

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

    public static void main(String[] args) {
        try {
            new Frontend().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        MessageConnection localhost = MSUtil.getConnection();
        session = localhost.getSession();

        session.declareTopic(basic);

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <T extends DataSet> Optional<T> load(long id, Class<T> cl) throws InterruptedException {
        final String reply = Messages.getRandomId();

        session.subscribe(reply);
        final Header head = new Messages.BasicProperties.Builder()
                .topic(basic)
                .replyTo(reply)
                .id(Messages.getRandomId())
                .addAttribute("action", "load")
                .addAttribute("class", User.class.getCanonicalName())
                .build();

        final ArrayBlockingQueue<Optional<T>> response = new ArrayBlockingQueue<>(1);
        session.addFilter(reply, message -> {
            log.info("incoming message: " + message);
            if (head.getId().equals(message.getId())) {
                try {
                    final String body = (String) message.getBody();
                    if ("null".equals(body)) {
                        response.add(Optional.empty());
                        return;
                    }
                    response.add(Optional.of(GsonUtil.fromJson(body, cl)));

                } catch (NumberFormatException e ) {
                    log.error("", e);
                }
            }
        });

        session.sendMessage(""+id, head);
        final Optional<T> take = response.take();
        session.unsubscribe(reply);
        return take;
    }

    private void save(User user) throws InterruptedException {
        final String reply = Messages.getRandomId();
        session.subscribe(reply);

        final Header head = new Messages.BasicProperties.Builder()
                .topic(basic)
                .replyTo(reply)
                .id(Messages.getRandomId())
                .addAttribute("action", "save")
                .addAttribute("class", User.class.getCanonicalName())
                .build();

        final ArrayBlockingQueue<Long> response = new ArrayBlockingQueue<>(1);
        session.addFilter(reply, message -> {
            if (head.getId().equals(message.getId())) {

                try {
                    response.add(Long.parseLong((String)message.getBody()));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        session.sendMessage(GsonUtil.toJson(user), head);
        final Long take = response.take();
        session.unsubscribe(reply);
        user.setId(take);
    }
}
