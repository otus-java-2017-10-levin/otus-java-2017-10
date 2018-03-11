package ru.orus.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Message header
 *
 * Implementation must be immutable
 */
@SuppressWarnings("unused")
public interface Header extends Serializable {

    @NotNull
    String getTopic();

    @NotNull
    String getId();

    Optional<String> getReplyTo();

    void addAttribute(@NotNull String name, @NotNull String value);

    Optional<String> getAttribute(@NotNull String name);

    @NotNull
    Map<String, String> getAttributes();

    Message.Type getType();
}
