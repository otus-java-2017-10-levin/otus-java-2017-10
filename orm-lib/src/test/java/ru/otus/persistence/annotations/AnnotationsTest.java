package ru.otus.persistence.annotations;


import org.junit.jupiter.api.Test;
import ru.otus.base.Address;
import ru.otus.base.UserDataSet;
import ru.otus.classes.TestDataSet;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AnnotationsTest {

    private final AnnotatedClass annotatedClass = AnnotatedClassImpl.of(UserDataSet.class);

    @Test
    void getArrayOfFields() {
        List<AnnotatedField> fields = annotatedClass.getFields();

        assertEquals(4, fields.size());
    }

    @Test
    void ifObjectIsEntity() {
        UserDataSet set = new UserDataSet("Flow");
        Address phone = new Address("100");
        AnnotatedClass AnnotatedClass = AnnotatedClassImpl.of(UserDataSet.class);

        assertEquals(true, AnnotatedClass.is(set));
        assertEquals(false, AnnotatedClass.is(phone));
    }

    @Test
    void getSimpleClassName() {
        AnnotatedClass AnnotatedClass = AnnotatedClassImpl.of(UserDataSet.class);

        assertEquals("UserDataSet", AnnotatedClass.getSimpleName());
    }

    @Test
    void testCache() {
        AnnotatedClass anno1 = AnnotatedClassImpl.of(UserDataSet.class);
        AnnotatedClass anno2 = AnnotatedClassImpl.of(UserDataSet.class);

        assertEquals(anno1, anno2);
    }

    @Test
    void getFieldByNameInBase() {
        AnnotatedClass user = AnnotatedClassImpl.of(UserDataSet.class);

        String dbName = "NAME";
        AnnotatedField f = user.getField(dbName);

        assertEquals("name", f.getName());
    }

    @Test
    void containsAnnotationTest() {
        AnnotatedClass cl = AnnotatedClassImpl.of(TestDataSet.class);

        AnnotatedField field = cl.getField("age");

        assertEquals(true, field.contains(Column.class));
        assertEquals(true, field.contains(OneToMany.class));
        assertEquals(false, field.contains(Id.class));

        assertEquals(2, field.getAnnotationCount());
    }

    @Test
    void getAnnotationTest() {
        AnnotatedClass cl = AnnotatedClassImpl.of(TestDataSet.class);

        AnnotatedField field = cl.getField("age");

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