package ru.otus.utils;

import ru.otus.mvc.model.Statistics;

import javax.management.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.management.ManagementFactory;

public final class JpaUtil {
    private static final String PERSISTENCE_UNIT_NAME = "otusJPAH2";
    private static ObjectName objectName;
    private static final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    static {
        try {
            objectName = new ObjectName("javax.cache:type=CacheStatistics," +
                                       "CacheManager=my-cache-jsr107,Cache=L1-Cache-0");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }

    public static void updateCacheStatistic(Statistics statistics) {
        try {
            statistics.setMisses((long)mBeanServer.getAttribute(objectName,"CacheMisses"));
            statistics.setHits((long)mBeanServer.getAttribute(objectName,"CacheHits"));
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException e) {
            e.printStackTrace();
        }

    }
}
