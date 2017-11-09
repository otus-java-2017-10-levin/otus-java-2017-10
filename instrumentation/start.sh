#!/usr/bin/env bash

mvn clean install
java -javaagent:target/instrumentation-jar-with-dependencies.jar  -jar target/instrumentation-jar-with-dependencies.jar
