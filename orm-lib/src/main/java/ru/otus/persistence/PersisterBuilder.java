package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DbManager;

public interface PersisterBuilder {

    @NotNull
    PersisterBuilder setDbManager(@NotNull DbManager manager);

    @NotNull
    PersisterBuilder setAnnotationManager(@NotNull AnnotationManager manager);

    @NotNull
    Persister build();
}
