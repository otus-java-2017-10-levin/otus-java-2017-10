package ru.otus.persistence;
/*
 *  Copyright by Flow on 18.12.2017.
 
    Description here
 */

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.util.Map;

public class MyPersistenceProvider implements PersistenceProvider {

    /**
     * Called by Persistence class when an {@link EntityManagerFactory}
     * is to be created.
     *
     * @param emName The name of the persistence unit
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
        return new MyEntityManagerFactory(emName);
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

    /**
     * Create database schemas and/or tables and/or create DDL
     * scripts as determined by the supplied properties.
     * <p>
     * Called by the container when schema generation is to
     * occur as a separate phase from creation of the entity
     * manager factory.
     *
     * @param info metadata for use by the persistence provider
     * @param map  properties for schema generation; these may also include provider-specific properties
     * @throws PersistenceException if insufficient or inconsistent configuration information is
     *                              provided or if schema generation otherwise fails.
     */
    @Override
    public void generateSchema(PersistenceUnitInfo info, Map map) {

    }

    /**
     * Create database schemas and/or tables and/or create DDL
     * scripts as determined by the supplied properties.
     * Called by the Persistence class when schema generation is to
     * occur as a separate phase from creation of the entity
     * manager factory.
     *
     * @param persistenceUnitName the name of the persistence unit
     * @param map                 properties for schema generation; these may also contain provider-specific properties. The value of
     *                            these properties override any values that may have been configured elsewhere.
     * @return true if schema was generated, otherwise false
     * @throws PersistenceException if insufficient or inconsistent configuration information is
     *                              provided or if schema generation otherwise fails.
     */
    @Override
    public boolean generateSchema(String persistenceUnitName, Map map) {
        return false;
    }

    /**
     * Return the utility interface implemented by the persistence
     * provider.
     *
     * @return ProviderUtil interface
     * @since Java Persistence 2.0
     */
    @Override
    public ProviderUtil getProviderUtil() {
        return null;
    }
}
