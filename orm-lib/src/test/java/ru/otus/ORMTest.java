package ru.otus;

import org.junit.jupiter.api.Test;
import ru.otus.base.UsersDataSet;
import ru.otus.orm.OrmLib;

public class ORMTest {

    private OrmLib myOrm = new OrmLib();

    @Test
    void OrmCreateTable() {
        myOrm.createTable(UsersDataSet.class);
    }

    @Test
    void saveObjectToOrm() {
        myOrm.createTable(UsersDataSet.class);
        UsersDataSet set = new UsersDataSet(1, "flow");
        myOrm.save(set);
    }
}