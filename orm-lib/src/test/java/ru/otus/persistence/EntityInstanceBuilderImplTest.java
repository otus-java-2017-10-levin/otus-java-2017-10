package ru.otus.persistence;

import org.junit.jupiter.api.Test;
import ru.otus.classes.AddressDataSet;
import ru.otus.classes.PhoneDataSet;
import ru.otus.persistence.fields.EntityInstance;
import ru.otus.persistence.fields.EntityInstanceBuilder;
import ru.otus.persistence.fields.EntityInstanceBuilderImpl;
import ru.otus.persistence.fields.SimpleField;

import javax.persistence.Id;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityInstanceBuilderImplTest {

    private EntityInstanceBuilder builder = new EntityInstanceBuilderImpl();
    private AnnotationManager man = new AnnotationManager(Id.class, AddressDataSet.class, PhoneDataSet.class);
    @Test
    void build() {
        AddressDataSet address = new AddressDataSet();
        PhoneDataSet phone = new PhoneDataSet();
        phone.setNumber("123-11-11");
        address.setCity("Moscow");
        address.setStreet("Red square");
        address.setPhone(phone);
        EntityInstance<AddressDataSet> userEntity = builder.build(address, man);

        List<SimpleField<?>> simples = userEntity.getSimpleFields();
        assertEquals(0, userEntity.getArrayFields().size());
        assertEquals(3, simples.size());
        assertEquals(1, userEntity.getEntitiesFields().size());

        SimpleField<?> simpleField1 = simples.get(0);
        assertEquals("city", simpleField1.getName());
        assertEquals("Moscow", simpleField1.getValue());

        SimpleField<?> simpleField2 = simples.get(1);
        assertEquals("street", simpleField2.getName());
        assertEquals("Red square", simpleField2.getValue());
    }
}