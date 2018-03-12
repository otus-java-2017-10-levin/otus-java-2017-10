package ru.orus.connection;

import ru.orus.core.MessageWorker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class SimpleMessageConnection implements MessageConnection {
    private final List<MessageSession> sessions = new CopyOnWriteArrayList<>();
    private final MessageWorker worker;

    SimpleMessageConnection(MessageWorker worker) {
        this.worker = worker;
    }

    @Override
    public MessageSession getSession() {
        final SimpleMessageSession session = new SimpleMessageSession(worker);
        sessions.add(session);
        return session;
    }

    @Override
    public void close() {
        sessions.forEach(MessageSession::close);
        worker.close();
    }
}
