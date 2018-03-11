package ru.orus.core.handlers;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.orus.common.Validation;
import ru.orus.core.AbstractMessageProcessor;
import ru.orus.core.Command;
import ru.orus.messages.Header;
import ru.orus.messages.Message;
import ru.orus.messages.Messages;

/**
 *  Handles system messages like:
 *  subscribe basic
 *
 *  More formally format is: command channel_name
 *
 */
public class SubscribeMessageHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(SubscribeMessageHandler.class);

    @Override
    public @NotNull Message<?> handleMessage(@NotNull Message<?> message, AbstractMessageProcessor processor) {

        if (Message.Type.SYSTEM == message.getHeader().getType()) {

            final String body = (String) message.getBody();
            if (body.equals(""))
                return message;

            log.debug("creating system message");

            final Header head = new Messages.BasicProperties.Builder()
                    .setType(Message.Type.COMMAND).topic(Messages.getSystemChannelName()).build();

            Command command = getSystemCommand(message, processor);
            return Messages.newMessage(command, head);
        }
        log.debug("regular message");
        return message;
    }

    private Command getSystemCommand(@NotNull Message<?> message,
                                     @NotNull AbstractMessageProcessor subscriber) {

        final String body = (String) message.getBody();

        Validation.validateNonNull("", body, subscriber);

        final String[] parse = parse(body);

        log.debug("command: " + body);
        if (parse.length != 2) {
            throw new IllegalArgumentException();
        }

        final String command = parse[0];
        final String topic = parse[1];
        if ("subscribe".equals(command)) {
            log.debug("creating subscribe command for topic: " + topic);
             return proc -> {
                proc.subscribe(topic, subscriber);
                subscriber.subscribe(topic, proc);
                subscriber.submit(message);
            };
        }
        if ("declare".equals(command)) {
            log.debug("declare topic command for " + topic);
            return proc -> {
              subscriber.subscribe(topic, proc);
              subscriber.submit(message);
            };
        }
        throw new IllegalStateException();
    }

    private String[] parse(@NotNull String command) {
        return command.split("\\s+");
    }
}