package ru.otus.annotations;

import org.junit.jupiter.api.Test;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UsersDataSet;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class AnnotationsTest {

    private AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

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
        UsersDataSet set = new UsersDataSet(1, "Flow");
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
}