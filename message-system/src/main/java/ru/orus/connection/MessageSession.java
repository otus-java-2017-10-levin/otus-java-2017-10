package ru.orus.connection;

import ru.orus.messages.Header;
import ru.orus.messages.Message;

import java.util.function.Consumer;

/**
 * Per thread session for working with message-system
 * <p>
 * Single worker for multiple session.
 * We need to manage message delivery from worker to session.
 * <p>
 * Consider that we have 2 topics A and B. Session A gets messages from topic A, session B - from B.
 * When we register session A topic, we shall subscribe in worker for topic A update.
 * This lead us to Pub/sub pattern.
 */
@SuppressWarnings("ALL")
public interface MessageSession {

    /**
     * Bind topic for session.
     *
     * @param topic - name of queue
     * @throws IllegalStateException - if topic is already set
     * @throws IllegalStateException - when queue is closed
     */
    void declareTopic(String topic);

    /**
     * Sends message to queue (specified in {@link MessageSession#declareTopic(String)}.
     * If queue is not specified - throws IllegalStateException
     *
     * @param message - text to send
     * @throws IllegalStateException - when queue is not specified
     * @throws IllegalStateException - when queue is closed
     */
    void sendMessage(String message);

    /**
     * Sends message to queue (specified in {@link MessageSession#declareTopic(String)}.
     * If queue is not specified - throws IllegalStateException
     *
     * @param message - text to send
     * @param props   - header properties
     * @throws IllegalStateException - when queue is not specified
     * @throws IllegalStateException - when queue is closed
     */
    void sendMessage(String message, Header props);

    /**
     * Set consumer for incoming messages.
     *
     * @param consumer - actions on incoming message
     */
    void addFilter(String topic, Consumer<Message<?>> consumer);

    /**
     * Subscribe for topic
     * @param topic - topic name
     */
    void subscribe(String topic);

    /**
     * Stop receiving messages from message server for {@code topic}
     * @param topic - topic to unsubscribe
     */
    void unsubscribe(String topic);

    String getTopic();

    /**
     * Close session.
     */
    void close();

    /**
     * @return - true if session is closed
     */
    boolean isClosed();
}
