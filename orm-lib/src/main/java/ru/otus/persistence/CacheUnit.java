package ru.otus.persistence;

import lombok.Data;

@Data
final class CacheUnit {
    private final long id;
    private final Class<?> name;
}
