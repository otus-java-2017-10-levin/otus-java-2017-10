package ru.orus.messages;

import ru.orus.common.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 *  @author Flow
 */
@SuppressWarnings("ALL")
public class Messages {
    public static class BasicProperties {
        public static class Builder {
            private String id;
            private String topic;
            private String replyTo;
            private final Map<String, String> attrs = new HashMap<>();

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

            public Header build() {
                id = id == null ? getRandomId() : id;
                if (topic == null)
                    throw new IllegalArgumentException("You should specify topic first");

                final MessageHeader messageHeader = new MessageHeader(topic, id, replyTo);
                attrs.forEach(messageHeader::addAttribute);
                return messageHeader;
            }
        }
    }

    public static Message newMessage(String message, Header header) {
        Validation.validateNonNull("", message, header);

        return StringMessage.of(message, header);
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }
}