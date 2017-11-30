package ru.otus.io;

/**
 *  Class contains information about input command.
 */

class Option {

    private String name;
    private boolean hasArgs;
    private String description;

    public Option(String name, boolean hasArgs, String description) {
        if (name == null || name.equals(""))
            throw new IllegalArgumentException();

        this.name = name;
        this.hasArgs = hasArgs;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean getHasArgs() {
        return hasArgs;
    }

    public String getDescription() {
        return description;
    }
}