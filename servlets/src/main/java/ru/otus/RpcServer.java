package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.connection.MessageConnection;
import ru.orus.connection.MessageSession;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;
import ru.otus.db.dao.UserDAO;
import ru.otus.db.entities.User;
import ru.otus.utils.GsonUtil;
import ru.otus.utils.JpaUtil;
import ru.otus.utils.MSUtil;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Optional;

public class RpcServer extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);
    private static final String topic = "basic";
    private MessageSession session;
    private EntityManager entityManager;


    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        MessageConnection localhost = MSUtil.getConnection();
        entityManager = JpaUtil.getFactory().createEntityManager();
        session = localhost.getSession();

        session.declareTopic(topic);

        session.addFilter(topic, message -> {
            log.info("incoming message:" + message);
            final String action = message.getHeader().getAttribute("action").orElseThrow(IllegalAccessError::new);

            if ("save".equals(action)) {
                saveEntity(message);
                return;
            }

            if ("load".equals(action)) {
                loadEntity(message);
                return;
            }
            throw new IllegalStateException("not supported action");
        });
    }

    private void loadEntity(Message message) {
        String cl = message.getHeader()
                .getAttribute("class")
                .orElseThrow(IllegalStateException::new);

        Optional<String> replyTo = message.getHeader().getReplyTo();
        Header head = new Messages.BasicProperties.Builder()
                .id(message.getId())
                .topic(replyTo.orElseThrow(IllegalStateException::new))
                .build();
        try {
            String json = loadFromDb(Long.parseLong(message.getBody()), cl);
            session.sendMessage("" + json, head);
        } catch (IllegalStateException e) {
            session.sendMessage(e.getCause().getMessage(), head);
        }
    }

    private void saveEntity(Message message) {
        String cl = message.getHeader().getAttribute("class")
                .orElseThrow(IllegalStateException::new);

        Optional<String> replyTo = message.getHeader().getReplyTo();

        Header head = new Messages.BasicProperties.Builder()
                .id(message.getId())
                .topic(replyTo.orElseThrow(IllegalStateException::new))
                .build();

        try {
            long id = saveToDb(message.getBody(), cl);
            session.sendMessage("" + id, head);
        } catch (IllegalStateException e) {
            session.sendMessage(e.getCause().getMessage(), head);
        }
    }

    private String loadFromDb(long id, String cl) {
        if ("ru.otus.db.entities.User".equals(cl)) {
            return loadUser(id, User.class);
        }
        throw new IllegalStateException("Wrong class to load");
    }

    private long saveToDb(String body, String s) throws IllegalStateException {
        if ("ru.otus.db.entities.User".equals(s)) {
            return saveUser(GsonUtil.fromJson(body, User.class));
        }

        throw new IllegalStateException("Wrong class to save");
    }


    private long saveUser(User user) {
        UserDAO dao = new UserDAO(entityManager);
        dao.save(user);
        return user.getId();
    }

    private <T> String loadUser(long id, Class<T> userClass) {
        UserDAO dao = new UserDAO(entityManager);
        final User load = dao.load(id);
        return GsonUtil.toJson(load);
    }
}
