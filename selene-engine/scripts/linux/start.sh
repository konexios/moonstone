#!/bin/bash

cd /opt/selene
/usr/bin/java -DseleneConfig=$config/selene.properties -Dlog4j.configurationFile=config/log4j2.xml -Djava.library.path=/usr/lib/jni -jar lib/selene-engine.jar &
