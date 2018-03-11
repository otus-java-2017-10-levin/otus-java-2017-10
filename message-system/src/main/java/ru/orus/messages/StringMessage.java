package ru.orus.messages;

import org.jetbrains.annotations.NotNull;
import ru.orus.common.Validation;

final class StringMessage implements Message<String> {

    private final String body;
    private final Header header;
    private static final long serialVersionUID = 42L * 42L;

    @NotNull
    @Override
    public Header getHeader() {
        return header;
    }

    @NotNull
    @Override
    public String getBody() {
        return body;
    }

    @NotNull
    @Override
    public String getId() {
        return header.getId();
    }

    public static Message<String> of(String message, Header header) {
        Validation.validateNonNull("", message, header);

        return new StringMessage(message, new Messages.BasicProperties.Builder(header).build());
    }

    private StringMessage(String message, Header header) {
        this.body = message;
        this.header = header;
    }

    @Override
    public String toString() {
        return "StringMessage{" +
                "body='" + body + '\'' +
                ", header=" + header +
                '}';
    }
}