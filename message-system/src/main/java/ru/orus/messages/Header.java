package ru.orus.messages;

import java.io.Serializable;
import java.util.Optional;

/**
 * Message header
 *
 * Implementation must be immutable
 */
@SuppressWarnings("unused")
public interface Header extends Serializable {

    String getTopic();
    String getId();
    Optional<String> getReplyTo();
    void addAttribute(String name, String value);
    Optional<String> getAttribute(String name);
}
