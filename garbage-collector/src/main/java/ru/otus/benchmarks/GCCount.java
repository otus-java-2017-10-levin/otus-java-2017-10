package ru.otus.benchmarks;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import com.sun.management.GarbageCollectorMXBean;

class GCCount {
    private volatile String gcBeanName;
    private volatile GarbageCollectorMXBean gcMBean;

    public GCCount(String beanName) {
        gcBeanName = beanName;
    }

    private void initGCMBean() {
        if (gcMBean == null) {
            synchronized (GCCount.class) {
                if (gcMBean == null) {
                    gcMBean = getGCMBean();
                }
            }
        }
    }

    private GarbageCollectorMXBean getGCMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();

            return ManagementFactory.newPlatformMXBeanProxy(server,
                    "java.lang:type=GarbageCollector,name="+gcBeanName, GarbageCollectorMXBean.class);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public boolean getInfo() {
        // initialize GC MBean
        initGCMBean();
        try {
            System.out.println("\tCollection count: \t\t" + gcMBean.getCollectionCount() +"\n\tCollection time: \t\t" + (double)gcMBean.getCollectionTime() / 1000.0+ "s.");

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
        return true;
    }

    public String getCollector() {
        return gcBeanName;
    }
}