package ru.otus.utils;

import ru.otus.mvc.model.Statistics;

import javax.management.*;
import java.lang.management.ManagementFactory;

public final class CacheUtil {
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

    public Statistics getCacheStatistic() {
        final Statistics statistics = new Statistics();
        try {
            statistics.setMisses((long)mBeanServer.getAttribute(objectName,"CacheMisses"));
            statistics.setHits((long)mBeanServer.getAttribute(objectName,"CacheHits"));
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
        return statistics;
    }
}
