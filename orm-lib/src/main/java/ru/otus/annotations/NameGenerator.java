package ru.otus.annotations;

@FunctionalInterface
interface NameGenerator {
    String generate(String name);
}
