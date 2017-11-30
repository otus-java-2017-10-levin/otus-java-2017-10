package ru.otus.io;

public interface Parser {

    /**
     * Add new option to parser. If name is already exist in parser,
     * throw IllegalArgumentException
     * @param name - command name
     * @param hasArgs - true if command must has arguments
     * @param description - description for help
     * @throws IllegalArgumentException - throws if name already exist
     */
    void addOption(String name, boolean hasArgs, String description) throws IllegalArgumentException;

    /**
     * Checks if command {@code name} exist in parser instance.
     * @param name - command name to check
     * @return - true if command {@code name} exist, else false
     */
    boolean hasOption(String name);

    /**
     * Convert input line to Command.
     * Checks the first word in {@code line}
     * @param line - text to parse
     * @param cmd - object to write
     * @throws IllegalArgumentException - throws if first word
     * in {@code line} does not valid.
     */
    void parse(String line, Command cmd) throws IllegalArgumentException;

    /**
     * Returns String contains usage text
     * @return usage text
     */
    String getHelp();
}