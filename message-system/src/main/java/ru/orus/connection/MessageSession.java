package ru.orus.connection;

import ru.orus.messages.Header;
import ru.orus.messages.Message;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
     * Bind queue for session.
     *
     * @param topicName - name of queue
     * @throws IllegalStateException - if topicName is already set
     * @throws IllegalStateException - when queue is closed
     */
    void declareTopic(String topicName);

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
     * Sends message to server and wait for reply at most {@code timeout} units of {@code units}
     * Reply message must have the same message id as an original message.
     * Server can reply to channel different from message topic.
     * This is specified by {@link ru.orus.messages.Messages.BasicProperties.Builder#replyTo(String)} method.
     * That means that session should be subcribed for replyTo channel.
     * @param message - message to send
     * @param props - message header
     * @param units - time units
     * @param timeout - time amount
     * @return - reply from server.
     */
    Future<Message<?>> sendMessage(String message, Header props, TimeUnit units, long timeout);
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
