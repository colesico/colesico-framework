#!/bin/bash
JAVA_PATH=/java/jdk11/bin/
${JAVA_PATH}java -Xmx25m -cp target/lib/*:target/helloworld.jar colesico.framework.example.helloworld.Main