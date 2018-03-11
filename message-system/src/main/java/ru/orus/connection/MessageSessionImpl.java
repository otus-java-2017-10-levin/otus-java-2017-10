package ru.orus.connection;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.ExecutorHelper;
import ru.orus.common.Validation;
import ru.orus.core.AbstractMessageProcessor;
import ru.orus.core.Subscription;
import ru.orus.core.Command;
import ru.orus.core.handlers.MessageHandler;
import ru.orus.core.handlers.MessageHandlerFactory;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

final class MessageSessionImpl extends AbstractMessageProcessor implements MessageSession {
    private final static Logger log = LoggerFactory.getLogger(MessageSessionImpl.class);
    private String topicName;
    private final Map<String, Consumer<Message<?>>> filters = new ConcurrentHashMap<>();
    private boolean isClosed;
    private MessageHandler handler = MessageHandlerFactory.getHandler();
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    private final Set<String> subscribedTopics = new HashSet<>();

    MessageSessionImpl() {
        isClosed = false;
        subscribedTopics.add(Messages.getSystemChannelName());
    }

    @Override
    public void declareTopic(String topicName) {
        this.topicName = topicName;
        final Header header = new Messages.BasicProperties.Builder().topic(Messages.getSystemChannelName()).setType(Message.Type.SYSTEM).build();
        final Future<Message<?>> result = sendMessage("declare " + topicName, header, TimeUnit.SECONDS, 2);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot declare topic!", e);
        } catch (CancellationException e) {
            log.error("Declaring canceled!", e);
        }
    }

    @Override
    public void sendMessage(String message) {
        final Header header = new Messages.BasicProperties.
                Builder().topic(topicName).build();
        sendMessage(message, header);
    }

    @Override
    public void sendMessage(String message, Header props) {
        final Message<?> message1 = Messages.newMessage(message, props);
        submit(handler.handleMessage(message1, this));
    }

    @Override
    public Future<Message<?>> sendMessage(@NotNull String message, @NotNull Header props, TimeUnit units, long timeout) {
        Validation.validateNonNull("", message, props);
        final String reply = props.getReplyTo().orElse(props.getTopic());
        final Message<String> msg = Messages.newMessage(message, props);

        Callable<Message<?>> task = () -> {
            final ArrayBlockingQueue<Message<?>> response = new ArrayBlockingQueue<>(1);
            this.addFilter(reply, message1 -> {
                if (msg.getId().equals(message1.getId())) {
                    response.add(message1);
                }
            });
            return response.take();
        };

        final Future<Message<?>> result = service.submit(task);
        sendMessage(message, props);
        service.schedule(() -> result.cancel(true), timeout, units);
        return result;
    }

    @Override
    public void addFilter(String topic, Consumer<Message<?>> consumer) {
        filters.put(topic, consumer);
        log.info("add filter: " + filters);
    }

    @Override
    public void subscribe(String topic) {
        if (!subscribedTopics.contains(topic)) {
            final Header header = new Messages.BasicProperties.Builder().topic(Messages.getSystemChannelName()).setType(Message.Type.SYSTEM).build();
            final Future<Message<?>> result = sendMessage("subscribe " + topic, header, TimeUnit.SECONDS, 20);
            try {
                result.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Cannot subscribe!", e);
                return;
            } catch (CancellationException e) {
                log.error("Subscribing canceled!", e);
                return;
            }
            subscribedTopics.add(topic);
        }
    }

    @Override
    public String getTopic() {
        return topicName;
    }

    @Override
    public void close() {
        subscriptions.forEach((s, subscription) -> subscription.cancel());
        subscriptions.clear();
        filters.clear();
        subscribedTopics.clear();
        ExecutorHelper.shutdownAndAwaitTermination(service, 2);
        isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }


    @Override
    public void onNext(@NotNull Subscription subscription, @NotNull Message item) {
        subscription.request(1);
        applyFilters(item);
    }

    private void applyFilters(Message mes) {
        final String topic = mes.getHeader().getTopic();
        log.info("filters: " + filters);
        log.info("message: " + mes);
        if (filters.containsKey(topic)) {
            filters.get(topic).accept(mes);
        }
    }

    @Override
    public void onComplete() {
        log.info("onComplete(). Close session");
        close();
    }

    @Override
    public void onError(@NotNull Throwable throwable) {
        log.error("", throwable);
        close();
    }

    @Override
    protected void onReceiveCommandMessage(@NotNull Command message) {
        message.execute(this);
    }
}