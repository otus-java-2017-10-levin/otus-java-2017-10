package ru.orus.messages;

import ru.orus.common.Validation;
import ru.orus.core.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 *  @author Flow
 */
@SuppressWarnings("ALL")
public class Messages {
    private static final String SYSTEM = "system";

    public static class BasicProperties {
        public static class Builder {
            private String id;
            private String topic;
            private Message.Type type;
            private String replyTo;
            private Map<String, String> attrs = new HashMap<>();

            public Builder() {}

            public Builder(Header header) {
                Validation.validateNonNull("", header);
                this.id = header.getId();
                this.topic = header.getTopic();
                this.type = header.getType();
                this.replyTo = header.getReplyTo().orElse(null);
                this.attrs = new HashMap<>(header.getAttributes());
            }

            public Builder id(String id) {
                this.id = id;
                return this;
            }

            public Builder topic(String topic) {
                this.topic = topic;
                return this;
            }

            public Builder replyTo(String addr) {
                Validation.validateNonNull("", addr);

                replyTo = addr;

                return this;
            }

            public Builder addAttribute(String name, String value) {
                Validation.validateNonNull("", name, value);

                attrs.put(name, value);
                return this;
            }

            public Builder setType(Message.Type type) {
                this.type = type;
                return this;
            }

            public Header build() {
                id = id == null ? getRandomId() : id;
                if (topic == null)
                    throw new IllegalArgumentException("You should specify topic first");

                type = type == null ? Message.Type.STRING : type;

                final MessageHeader messageHeader = new MessageHeader(topic, id, replyTo, type);
                attrs.forEach(messageHeader::addAttribute);
                return messageHeader;
            }
        }
    }

    public static Message<String> newMessage(String message, Header header) {

        return StringMessage.of(message, header);
    }

    public static Message<Command> newMessage(Command message, Header header) {
        return CommandMessage.of(message, header);
    }

    public static Message<String> newSystemMessage(String message, Header header) {
        return SystemMessage.of(message, header);
    }


    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }

    public static final String getSystemChannelName() {
        return SYSTEM;
    }
}