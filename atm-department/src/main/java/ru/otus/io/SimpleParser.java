package ru.otus.io;

import java.util.*;

public class SimpleParser implements Parser {

    private final Map<String, Option> options = new HashMap<>();

    /**
     * Add new option to parser. If name is already exist in parser,
     * throw IllegalArgumentException
     *
     * @param name        - command name
     * @param hasArgs     - true if command must has arguments
     * @param description - description for help
     * @throws IllegalArgumentException - throws if name already exist
     */
    @Override
    public void addOption(String name, boolean hasArgs, String description) throws IllegalArgumentException {
        if (name == null || description == null || name.equals(""))
            throw new IllegalArgumentException();

        if (hasOption(name))
            throw new IllegalArgumentException();

        options.put(name, new Option(name, hasArgs, description));
    }

    /**
     * Checks if command {@code name} exist in parser instance.
     *
     * @param name - command name to check
     * @return - true if command {@code name} exist, else false
     */
    @Override
    public boolean hasOption(String name) {
        return options.containsKey(name);
    }

    /**
     * Convert input line to Action.
     * Checks the first word in {@code line}
     * Checks if selected Option has attributes
     * @param line - text to parse
     * @param cmd - Command to write
     * @throws IllegalArgumentException - throws if first word
     * in {@code line} does not valid.
     * Throws if Option instance {@code hasArgs}
     * is true and there are no args in {@code line}.
     * Throws if Option instance {@code hasArgs}
     * is false and there are args in {@code line}.
     */
    @Override
    public void parse(String line, Command cmd) throws IllegalArgumentException {
        if (line == null || line.equals(""))
            throw new IllegalArgumentException();

        Queue<String> words = new ArrayDeque<>(Arrays.asList(line.split("\\s+")));
        String name = words.poll();
        if (hasOption(name)) {
            Option opt = options.get(name);

            if ( checkQueue(opt.getHasArgs(), words.isEmpty()) ) {
                cmd.setName(name);
                cmd.setParams(words);
            } else {
                throw new IllegalArgumentException("Wrong parameter list");
            }

        } else {
            throw new IllegalArgumentException("Wrong command");
        }
    }

    private boolean checkQueue(boolean hasArgs, boolean queueIsEmpty) {
        return hasArgs && !queueIsEmpty ||
                !hasArgs && queueIsEmpty;
    }

    /**
     * Returns String contains usage text
     *
     * @return usage text
     */
    @Override
    public String getHelp() {
        StringBuilder builder = new StringBuilder("usage: command [args]\nCommands:\n");
        for (Map.Entry<String, Option> com: options.entrySet()) {
            Option opt = com.getValue();
            builder.append("\t").append(com.getKey());
            if (opt == null) {
                throw new IllegalStateException("Opt is null");
            }
            if (opt.getHasArgs())
                builder.append(" args");

            builder.append("\t\t").append(opt.getDescription()).append("\n");
        }
        return builder.toString();
    }

}