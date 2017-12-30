package ru.otus.persistence.annotations;


import org.junit.jupiter.api.Test;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UsersDataSet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AnnotationsTest {

    private final AnnotatedClass annotatedClass = AnnotatedClass.of(UsersDataSet.class);

    @Test
    void loadAnnotations() {

        AnnotatedField idField = annotatedClass.getId();

        assertEquals(true, idField.getName().equals("id"));
    }

    @Test
    void getArrayOfFields() {
        List<AnnotatedField> fields = annotatedClass.getFields();

        assertEquals(2, fields.size());
    }

    @Test
    void ifObjectIsEntity() {
        UsersDataSet set = new UsersDataSet("Flow");
        PhoneDataSet phone = new PhoneDataSet("100", 1);
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

    @Test
    void getFieldByNameInBase() {
        AnnotatedClass user = AnnotatedClass.of(UsersDataSet.class);

        String dbName = "NAME";
        AnnotatedField f = user.getField(dbName);

        assertEquals("name", f.getName());
    }
}