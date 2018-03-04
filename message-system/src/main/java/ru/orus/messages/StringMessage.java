package ru.orus.messages;

import ru.orus.common.Validation;

final class StringMessage implements Message {

    private final String body;
    private final Header header;
    private static final long serialVersionUID = 42L * 42L;

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getId() {
        return header.getId();
    }
    public static Message of(String message, Header header) {
        Validation.validateNonNull("", message, header);

        return new StringMessage(message, header);
    }

    private StringMessage(String message, Header header) {
        this.body = message;
        this.header = header;
    }

    @Override
    public String toString() {
        return "StringMessage{" +
                "header='" + header + "'" +
                "body='" + body + '\'' +
                '}';
    }
}