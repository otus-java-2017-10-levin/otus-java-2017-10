package ru.orus.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.core.AbstractMessageProcessor;
import ru.orus.messages.Messages;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class MessageConnectionImpl implements MessageConnection {
    private final static Logger log = LoggerFactory.getLogger(MessageConnectionImpl.class);
    private final AbstractMessageProcessor worker;
    private final List<MessageSession> sessions = new CopyOnWriteArrayList<>();

    MessageConnectionImpl(AbstractMessageProcessor worker) {
        this.worker = worker;
    }

    @Override
    public MessageSession getSession() {
        final MessageSessionImpl messageSession = new MessageSessionImpl();
        worker.subscribe(Messages.getSystemChannelName(), messageSession);
        messageSession.subscribe(Messages.getSystemChannelName(), worker);
        sessions.add(messageSession);
        log.info("Created new session for worker " + worker);
        return messageSession;
    }

    @Override
    public void close() {
        sessions.forEach(MessageSession::close);
    }
}
