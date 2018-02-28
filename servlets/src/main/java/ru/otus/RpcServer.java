package ru.otus;

import com.google.gson.Gson;
import ru.orus.connection.ConnectionFactory;
import ru.orus.connection.MessageConnection;
import ru.orus.connection.MessageSession;

import java.io.IOException;

public class RpcServer {
    private static final String HOST = "localhost";
    private static final String topic = "basic";
    private static final String replyTopic = "basic-response";
    private MessageConnection localhost;
    private MessageSession session;
    private Gson gson = new Gson();

    public void init() throws IOException {
        localhost = new ConnectionFactory().connect(HOST);
        session = localhost.getSession();
        session.setErrorHandler(e -> {
            localhost.close();
        });

        session.addFilter(topic, message -> {
            final String body = message.getBody();


        });
    }


}
