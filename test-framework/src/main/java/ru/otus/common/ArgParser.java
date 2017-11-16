package ru.otus.common;

import org.apache.commons.cli.*;

public class ArgParser {

    private final Options options = new Options();
    private CommandLine cmd;


    public void addOption(String name, boolean hasArg, String description) {
        options.addOption(name, hasArg, description);
    }

    public void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean hasOption(String name) {
        if (cmd == null)
            throw new IllegalStateException("Use parse first");

        return cmd.hasOption(name);
    }

    public String getOption(String name) {
        if (cmd == null)
            throw new IllegalStateException("Use parse first");

        return cmd.getOptionValue(name);
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "[option] {value]...", options );
    }
}