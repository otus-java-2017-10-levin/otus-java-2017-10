package ru.otus.persistence.annotations;


import org.junit.jupiter.api.Test;
import ru.otus.base.PhoneDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.classes.TestDataSet;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AnnotationsTest {

    private final AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

    @Test
    void loadAnnotations() {

        AnnotatedField idField = annotatedClass.getId();

        assertEquals(true, idField.getName().equals("id"));
    }

    @Test
    void getArrayOfFields() {
        List<AnnotatedField> fields = annotatedClass.getFields();

        assertEquals(3, fields.size());
    }

    @Test
    void ifObjectIsEntity() {
        UserDataSet set = new UserDataSet("Flow");
        PhoneDataSet phone = new PhoneDataSet("100", 1);
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        assertEquals(true, annotatedClass.is(set));
        assertEquals(false, annotatedClass.is(phone));
    }

    @Test
    void getSimpleClassName() {
        AnnotatedClass annotatedClass = AnnotatedClass.of(UserDataSet.class);

        assertEquals("UserDataSet", annotatedClass.getSimpleName());
    }

    @Test
    void testCache() {
        AnnotatedClass anno1 = AnnotatedClass.of(UserDataSet.class);
        AnnotatedClass anno2 = AnnotatedClass.of(UserDataSet.class);

        assertEquals(anno1, anno2);
    }


    @Test
    void createWithWrongClass() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> AnnotatedClass.of(String.class));
        assertEquals(null, e.getMessage());
    }

    @Test
    void getFieldByNameInBase() {
        AnnotatedClass user = AnnotatedClass.of(UserDataSet.class);

        String dbName = "NAME";
        AnnotatedField f = user.getField(dbName);

        assertEquals("name", f.getName());
    }

    @Test
    void containsAnnotationTest() {
        AnnotatedClass cl = AnnotatedClass.of(TestDataSet.class);

        AnnotatedField field = cl.getField("age");
        assert field != null;

        assertEquals(true, field.contains(Column.class));
        assertEquals(true, field.contains(OneToMany.class));
        assertEquals(false, field.contains(Id.class));

        assertEquals(2, field.getAnnotationCount());
    }

    @Test
    void getAnnotationTest() {
        AnnotatedClass cl = AnnotatedClass.of(TestDataSet.class);

        AnnotatedField field = cl.getField("age");
        assert field != null;

        Id id = field.getAnnotation(Id.class);
        assertEquals(null, id);

        Column col = field.getAnnotation(Column.class);
        assert col != null;
        assertEquals("age", col.name());

        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        assert oneToMany != null;
        assertEquals("test", oneToMany.mappedBy());
    }
}