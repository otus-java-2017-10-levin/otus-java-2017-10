package ru.orus.messages;

import ru.orus.common.Validation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class MessageHeader implements Header, Serializable {

    private static final long serialVersionUID = 42L * 42L * 42L;
    private final String topic;
    private final String idString;
    private final String replyTo;
    private final Map<String, String> attributes = new HashMap<>();

    @SuppressWarnings("unused")
    MessageHeader(String topic, String idString, String replyTo) {
        Validation.validateNonNull(topic);
        this.topic = topic;
        this.idString = idString;
        this.replyTo = replyTo;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getId() {
        return idString;
    }

    @Override
    public Optional<String> getReplyTo() {
        return Optional.of(replyTo);
    }

    @Override
    public void addAttribute(String name, String value) {
        Validation.validateNonNull("", name, value);
        attributes.put(name, value);
    }

    @Override
    public Optional<String> getAttribute(String name) {
        return Optional.of(attributes.get(name));
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "topic='" + topic + '\'' +
                ", idString='" + idString + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}