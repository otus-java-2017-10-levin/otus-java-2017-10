package ru.orus.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 *  Simple message interface
 *
 */
public interface Message<T> extends Serializable {

    enum Type {
        SYSTEM,
        STRING,
        COMMAND
    }

    @NotNull
    Header getHeader();
    @SuppressWarnings("unused")
    @NotNull
    T getBody();
    @NotNull
    String getId();
}