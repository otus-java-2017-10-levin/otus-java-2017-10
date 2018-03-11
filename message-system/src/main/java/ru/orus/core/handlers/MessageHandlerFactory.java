package ru.orus.core.handlers;

public class MessageHandlerFactory {
    private MessageHandlerFactory() {}

    public static MessageHandler getHandler() {
        return new SubscribeMessageHandler();
    }
}
