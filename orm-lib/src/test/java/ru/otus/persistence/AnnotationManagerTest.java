package ru.otus.persistence;

import org.junit.jupiter.api.Test;

import ru.otus.classes.Address;
import ru.otus.classes.UserDataSet;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationManagerTest {

    private final AnnotationManager man = new AnnotationManager(Id.class, UserDataSet.class, Address.class);

    @Test
    void getId() {
        assertEquals("id", man.getId(UserDataSet.class).getName());
        assertEquals(long.class, man.getId(UserDataSet.class).getType());
    }

    @Test
    void getIdAnnotation() {
        assertEquals(Id.class, man.getIdAnnotation());
    }

    @Test
    void validateClasses() {
        new AnnotationManager(Id.class, UserDataSet.class, Address.class);
    }
}