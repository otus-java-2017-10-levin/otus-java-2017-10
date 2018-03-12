package ru.orus.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 *  Simple message interface
 *
 */
@SuppressWarnings("unused")
public interface Message<T> extends Serializable {

    enum Type {
        SYSTEM,
        STRING
    }

    @NotNull
    Header getHeader();
    @SuppressWarnings("unused")
    @NotNull
    T getBody();
    @NotNull
    String getId();
}