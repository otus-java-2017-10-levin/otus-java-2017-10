package ru.orus;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;

class App {

    public static void main(String[] args) throws IOException {
        Configurator.setRootLevel(Level.DEBUG);
//        for (int i =0; i < 100; i++) {
            final Server server = new Server(5050);
            server.start();
//            server.stop();
//        }
    }
}