#!/usr/bin/env bash

REMOTE_DEBUG="-agentlib:jdwp=transport=dt_socket,address=14025,server=y,suspend=n"
MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m"
GC="-XX:+UseParallelGC -XX:-UseParallelOldGC"
GC_LOG=" -verbose:gc -Xloggc:./logs/gc_pid.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails"
JMX="-Dcom.sun.management.jmxremote.port=15025 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"
JARPARAMS="-m HASH -jconsole -young scavenge -old marksweep"

# young: parnew scavenge copy
# old: cms marksweep

"C:\Program Files\Java\jdk1.8.0_92\bin\java.exe" -XX:OnOutOfMemoryError="taskkill /F /PID %p" ${REMOTE_DEBUG} ${MEMORY} ${GC} ${GC_LOG} ${JMX} ${DUMP} -jar target/garbage-collector.jar ${JARPARAMS}

"C:\Program Files\Java\jdk1.8.0_92\bin\java.exe" -jar gcviewer-1.36-SNAPSHOT.jar logs/gc_pid.log