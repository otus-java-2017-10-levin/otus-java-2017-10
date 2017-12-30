package ru.otus.persistence.annotations;

@FunctionalInterface
interface NameGenerator {
    String generate(String name);
}
