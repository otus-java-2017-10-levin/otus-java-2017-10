package ru.otus;

import org.h2.tools.Server;
import ru.otus.base.PhonesDataSet;
import ru.otus.base.UserDataSet;
import ru.otus.dao.UserDataSetDAO;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Executable;
import java.sql.SQLException;

class App {

    private final String jpa = "otusJPAH2";
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(jpa);
    private final PersistenceParams params = new PersistenceParams(jpa, "META-INF/persistence.xml");


    public static void main(String[] args) throws SQLException {
        new App().run();
    }

    private void run() throws SQLException {

        startServer();
        UserDataSet user = new UserDataSet("Flow");
        PhonesDataSet phone = new PhonesDataSet("100", 1);
        user.setPhone(phone);
        phone.setUser(user);
        user.setAge(10);

        UserDataSetDAO dao = new UserDataSetDAO(factory.createEntityManager(params.getParameters()));
        dao.save(user);

        UserDataSet fromDB = dao.load(user.getId());
        System.out.println("After find by id:\n" + fromDB);
//        factory.close();

//        System.exit(0);
    }

    /*
    * EntityStructure{entity=UserDataSet:[id: 0; name: Flow; age: 10; employeeId: 0],
    *                 entityClass=AnnotatedClassImpl{
    *                       annotatedClass=class ru.otus.base.UserDataSet,
    *                       generator=ru.otus.persistence.annotations.AnnotatedClassImpl$$Lambda$1/1267032364@3ffc5af1,
    *                       fields={NAME=AnnotatedFieldImpl{field=private java.lang.String ru.otus.base.UserDataSet.name, annotations={}}, AGE=AnnotatedFieldImpl{field=private int ru.otus.base.UserDataSet.age, annotations={}}, EMPLOYEEID=AnnotatedFieldImpl{field=private long ru.otus.base.UserDataSet.employeeId, annotations={}}, PHONE=AnnotatedFieldImpl{field=private ru.otus.base.PhonesDataSet ru.otus.base.UserDataSet.phone, annotations={interface javax.persistence.OneToOne=@javax.persistence.OneToOne(optional=true, targetEntity=void, cascade=[], mappedBy=, fetch=EAGER)}}, ID=AnnotatedFieldImpl{field=protected long ru.otus.base.DataSet.id, annotations={interface javax.persistence.Id=@javax.persistence.Id()}}}}}

     * */
    private void startServer() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

    }
}