package ru.orus.messages;


import org.jetbrains.annotations.NotNull;
import ru.orus.common.Validation;
import ru.orus.core.Command;

final class CommandMessage implements Message<Command> {

    private final Command body;
    private final Header header;

    private CommandMessage(@NotNull Command body, @NotNull Header header) {
        this.body = body;
        this.header = header;
    }

    @NotNull
    public static Message<Command> of(Command body, Header header) {
        Validation.validateNonNull("", body, header);

        final Header head = new Messages.BasicProperties.Builder(header).setType(Type.COMMAND).build();
        return new CommandMessage(body, head);
    }

    @NotNull
    @Override
    public Header getHeader() {
        return header;
    }

    @NotNull
    @Override
    public Command getBody() {
        return body;
    }

    @NotNull
    @Override
    public String getId() {
        return header.getId();
    }
}
