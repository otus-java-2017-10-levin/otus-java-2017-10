package ru.orus;

import ru.orus.core.SimpleServer;

import java.io.IOException;

class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        final SimpleServer server = new SimpleServer(5050);
        server.start();
    }
}