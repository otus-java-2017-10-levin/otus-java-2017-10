package ru.otus.persistence;
/*
 *  Copyright by Flow on 18.12.2017.
 
    Description here
 */

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

public class MyPersistenceProvider implements PersistenceProvider {

    /**
     * Called by Persistence class when an {@link EntityManagerFactory}
     * is to be created.
     *
     * @param emName The phone of the persistence unit
     * @param map    A Map of properties for use by the
     *               persistence provider. These properties may be used to
     *               override the values of the corresponding elements in
     *               the persistence.xml file or specify values for
     *               properties not specified in the persistence.xml.
     * @return EntityManagerFactory for the persistence unit,
     * or null if the provider is not the right provider
     */
    @Override
    public EntityManagerFactory createEntityManagerFactory(String emName, Map map) {
        return new MyEntityManagerFactory();
    }

    /**
     * Called by the container when an {@link EntityManagerFactory}
     * is to be created.
     *
     * @param info Metadata for use by the persistence provider
     * @param map  A Map of integration-level properties for use
     *             by the persistence provider. Can be null if there is no
     *             integration-level property.
     * @return EntityManagerFactory for the persistence unit
     * specified by the metadata
     */
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
        return null;
    }
}
