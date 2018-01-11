package ru.otus.persistence.caching;

import lombok.Data;

@Data
final public class CacheUnit {
    private final long id;
    private final Class<?> name;
}
