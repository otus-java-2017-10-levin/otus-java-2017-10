package ru.orus.messages;

import java.io.Serializable;

/**
 *  Simple message interface
 *
 */
public interface Message extends Serializable {

    Header getHeader();
    @SuppressWarnings("unused")
    String getBody();
    String getId();
}