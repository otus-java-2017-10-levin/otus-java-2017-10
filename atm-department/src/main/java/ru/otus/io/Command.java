package ru.otus.io;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 *  Command class contains command name and params
 *  And several methods for working with params
 */

public class Command {

    private String name;
    private Queue<String> params;

    public Command() {}

    private Command(String name, Queue<String> params) {
        if (name == null || params == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.params = new ArrayDeque<>(params);
    }

    public Command(String name, String[] params) {
        this(name, new ArrayDeque<>(Arrays.asList(params)));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParams(Queue<String> params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getNext() {
        return params.poll();
    }

    /**
     * Convert getNext to long
     * @return converted long
     */
    public long getLong() {
        String tmp = getNext();
        if (tmp == null) {
            throw new IllegalStateException("Wrong parameter list");
        }

        return Long.parseLong(tmp);
    }
}
