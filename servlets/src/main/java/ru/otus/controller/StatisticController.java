package ru.otus.controller;

import ru.otus.model.Statistics;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.net.URI;

public class StatisticController {

    private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    private final ObjectName objectName;

    public StatisticController(ObjectName name) {
        this.objectName = name;
    }

    public void update(Statistics statistics) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
        statistics.setMisses((long)mBeanServer.getAttribute(objectName,"CacheMisses"));
        statistics.setHits((long)mBeanServer.getAttribute(objectName,"CacheHits"));
    }
}
