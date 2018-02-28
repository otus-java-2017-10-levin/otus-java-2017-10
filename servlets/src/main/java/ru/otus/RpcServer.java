package ru.otus;

import com.google.gson.Gson;
import ru.orus.connection.ConnectionFactory;
import ru.orus.connection.MessageConnection;
import ru.orus.connection.MessageSession;
import ru.orus.messages.Header;
import ru.orus.messages.Messages;
import ru.otus.db.dao.PhoneDAO;
import ru.otus.db.dao.UserDAO;
import ru.otus.db.entities.DataSet;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;
import ru.otus.utils.JpaUtil;
import ru.otus.utils.MSUtil;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Optional;

public class RpcServer {
    private static final String topic = "basic";
    private MessageSession session;
    private Gson gson = new Gson();
    private EntityManager entityManager;

    public void init() throws IOException {
        MessageConnection localhost = MSUtil.getConnection();
        entityManager = JpaUtil.getFactory().createEntityManager();
        session = localhost.getSession();
        session.setErrorHandler(e -> {
            localhost.close();
        });

        session.addFilter(topic, message -> {
            final String body = message.getBody();

            final String action = message.getHeader().getAttibute("action").orElseThrow(IllegalAccessError::new);

            switch (action) {
                case "save":
                    final String cl = message.getHeader()
                            .getAttibute("class")
                            .orElseThrow(IllegalStateException::new);

                    final Optional<String> replyTo = message.getHeader().getReplyTo();
                    final Header head = new Messages.BasicProperties.Builder()
                            .id(message.getId())
                            .topic(replyTo.orElseThrow(IllegalStateException::new))
                            .build();
                    try {
                        long id = saveToDb(body, cl);
                        session.sendMessage(""+id, head);
                    } catch (IllegalStateException e) {
                        session.sendMessage(e.getCause().getMessage(), head);
                    }
                    break;
            }
        });
    }

    private long saveToDb(String body, String s) throws IllegalStateException {
        if ("ru.otus.db.entities.User".equals(s)) {
            return saveUser(gson.fromJson(body, User.class));
        }

        throw new IllegalStateException("Wrong class to save");
    }


    private long saveUser(User user) {
        UserDAO dao = new UserDAO(entityManager);
        dao.save(user);
        return user.getId();
    }
}
