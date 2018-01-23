package ru.otus.controller;

import ru.otus.model.Statistics;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class StatisticsController {

    private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    private final ObjectName objectName;

    private final Statistics statistics;

    public StatisticsController(ObjectName objectName, Statistics statistics) {
        this.objectName = objectName;
        this.statistics = statistics;
    }

    public void update() throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
        statistics.setMisses((long)mBeanServer.getAttribute(objectName,"CacheMisses"));
        statistics.setHits((long)mBeanServer.getAttribute(objectName,"CacheHits"));
    }
}
