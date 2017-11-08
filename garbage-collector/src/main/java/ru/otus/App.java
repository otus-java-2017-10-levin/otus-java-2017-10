package ru.otus;

import ru.otus.benchmarks.Benchmark;
import ru.otus.common.ArgParser;
import ru.otus.leaks.LeakStrategies;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class App {

    private static final String METHOD = "m";
    private static final String JCONSOLE = "jconsole";
    private static final String YOUNG = "young";
    private static final String OLD = "old";
    private final Map<String, String> collectors = new HashMap<>();

    private String young;
    private String old;

    private App() {

        // young
        collectors.put("parnew", "ParNew");
        collectors.put("scavenge", "PS Scavenge");
        collectors.put("copy", "Copy");

        //old
        collectors.put("cms", "ConcurrentMarkSweep");
        collectors.put("marksweep", "PS MarkSweep");
        collectors.put("marksweepcmp", "MarkSweepCompact");
    }

    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    private void createOptions(ArgParser parser) {

        parser.addOption(METHOD, true, "leak method:\n " +
                "FINALIZE - using create objects in finalize() for memory leaks;\n" +
                "HASH - using wrong hashCode method for memory leak.");
        parser.addOption(JCONSOLE, false, "open jconsole");
        parser.addOption(YOUNG, true, "young collector to earn statistic. Have to be one of the following:\n" +
                "ParNew - ParNew collector\n" +
                "scavenge - PS Scavenge collector\n" +
                "copy - Copy collector");
        parser.addOption(OLD, true, "old collector to earn statistic. Have to be one of the following:\n" +
                "cms - ConcurrentMarkSweep collector\n" +
                "marksweep - PS MarkSweep collector\n" +
                "marksweepcmp - Mark Sweep Compact collector");
    }

    private boolean checkArgs(ArgParser parser) {
        if (!parser.hasOption(METHOD) || !parser.hasOption(YOUNG) || !parser.hasOption(OLD)) {
            parser.printUsage();
            return false;
        }

        young = parser.getOption(YOUNG).toLowerCase();
        old = parser.getOption(OLD).toLowerCase();

        if (!hasCollector(young) || !hasCollector(old)) {
            parser.printUsage();
            return false;
        }

        return true;
    }

    private void showGCInfo() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        Set names = mbs.queryNames(null, null);
        System.out.println(names.toString().replace(", ", System.getProperty("line.separator")));
    }

    private void run(String[] args) {
        ArgParser parser = new ArgParser();
        createOptions(parser);
        parser.parse(args);
        if (!checkArgs(parser))
            return;

        showGCInfo();

        young = getCollector(young);
        old = getCollector(old);

        LeakStrategies leak = parseMethod(parser.getOption(METHOD).toUpperCase());

        int pid = Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        if (parser.hasOption(JCONSOLE))
            runJconsole(pid);

        runMBean(leak);

    }

    private void runMBean(LeakStrategies strategy) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("ru.otus:type=Benchmark");
            Benchmark mBean = new Benchmark();
            mbs.registerMBean(mBean, name);
            mBean.setLeakStrategy(strategy);
            mBean.setYoungCollector(young);
            mBean.setOldCollector(old);
            mBean.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LeakStrategies parseMethod(String name) {
        LeakStrategies res = LeakStrategies.HASH;

        if (name.equals("FINALIZE"))
            res = LeakStrategies.FINALIZE;
        else if (name.equals("HASH")) {
            res = LeakStrategies.HASH;
        }

        return res;
    }

    private void runJconsole(int pid) {
        try {
            Runtime.getRuntime().exec("jconsole " + pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasCollector(String arg) {
        return collectors.containsKey(arg);
    }

    private String getCollector(String arg) {
        return collectors.get(arg);
    }

}