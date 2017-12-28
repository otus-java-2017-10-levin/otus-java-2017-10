package ru.otus.annotations;


import org.apache.commons.lang3.reflect.FieldUtils;
import org.h2.engine.UserDataType;
import org.junit.jupiter.api.Test;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UsersDataSet;
import ru.otus.persistence.PersistenceHelper;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class AnnotationsTest {

    private final AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

    @Test
    void loadAnnotations() {

        Field idField = annotatedClass.getId();

        assertEquals(true, idField.getName().equals("id"));
    }

    @Test
    void getArrayOfFields() {
        Set<Field> fields = annotatedClass.getFields();

        assertEquals(1, fields.size());
    }

    @Test
    void ifObjectIsEntity() {
        UsersDataSet set = new UsersDataSet("Flow");
        PhoneDataSet phone = new PhoneDataSet(1, "100");
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        assertEquals(true, annotatedClass.is(set));
        assertEquals(false, annotatedClass.is(phone));
    }

    @Test
    void getSimpleClassName() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

        assertEquals("UsersDataSet", annotatedClass.getSimpleName());
    }

    @Test
    void testCache() {
        AnnotatedClass anno1 = AnnotatedClass.of(UsersDataSet.class);
        AnnotatedClass anno2 = AnnotatedClass.of(UsersDataSet.class);

        assertEquals(anno1, anno2);
    }


    @Test
    void createWithWrongClass() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> AnnotatedClass.of(String.class));
        assertEquals(null, e.getMessage());
    }
}