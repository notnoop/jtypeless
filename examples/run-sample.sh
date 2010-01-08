#!/bin/sh

echo Compiling Sample.java
java -cp $JAVA_HOME/lib/tools.jar:../jtypeless.jar \
     com.sun.tools.javac.Main -d . Sample.java

echo Running Sample now
java -cp ../jtypeless.jar:. Sample
