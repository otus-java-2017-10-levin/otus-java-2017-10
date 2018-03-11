package ru.orus.messages;

import org.jetbrains.annotations.NotNull;
import ru.orus.common.Validation;

final class SystemMessage implements Message<String> {

    private final String body;
    private final Header header;

    private SystemMessage(@NotNull String body, @NotNull Header header) {
        this.body = body;
        this.header = header;
    }

    @NotNull
    public static Message<String> of(String body, Header header) {
        Validation.validateNonNull("", body, header);

        final Header head = new Messages.BasicProperties.Builder(header).setType(Type.SYSTEM).build();
        return new SystemMessage(body, head);
    }

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
}
