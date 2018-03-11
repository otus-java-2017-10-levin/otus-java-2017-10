package ru.orus.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.orus.common.Validation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class MessageHeader implements Header, Serializable {

    private static final long serialVersionUID = 42L * 42L * 42L;
    private final String topic;
    private final String idString;
    private final String replyTo;
    private final Map<String, String> attributes = new HashMap<>();
    private final Message.Type type;

    @SuppressWarnings("unused")
    MessageHeader(@NotNull String topic, @Nullable String idString, @Nullable String replyTo, Message.Type type) {
        Validation.validateNonNull(topic);
        this.topic = topic;
        this.idString = idString;
        this.replyTo = replyTo;
        this.type = type;
    }

    @NotNull
    @Override
    public String getTopic() {
        return topic;
    }

    @NotNull
    @Override
    public String getId() {
        return idString;
    }

    @Override
    public Optional<String> getReplyTo() {
        return Optional.ofNullable(replyTo);
    }

    @Override
    public void addAttribute(@NotNull String name, @NotNull String value) {
        Validation.validateNonNull("", name, value);
        attributes.put(name, value);
    }

    @Override
    public Optional<String> getAttribute(@NotNull String name) {
        return Optional.of(attributes.get(name));
    }

    @NotNull
    @Override
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes == null ? new HashMap<>() : attributes);
    }

    @Override
    public Message.Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "topic='" + topic + '\'' +
                ", idString='" + idString + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", attributes=" + attributes +
                ", type=" + type +
                '}';
    }
}