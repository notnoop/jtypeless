jTypeless  -  Java, but actually Groovy
------------------------------------

This sample project is inspired by
[typelessj](http://code.google.com/p/typelessj/) work, by
Michael Bayne and Michael Ernst.

This is a proof-of-concept implementation that treats Java code like
a Groovy code (with taking care of some edge cases), with ignoring all the
types!

This sample code illustrates two points:

- You can use an annotation processor to invoke a different specialized
  (dynamic?) compiler, without affecting the usability of the tool

- how you can generate Groovy transformations

Unfortunately, there are some rough edges here, but hey, this worked within
less than two hours of work.


Sample:

Consider the following class:

[Available in examples]
	class Sample {
    	private void test(Integer a) { System.out.println("Int: " + a); }

    	public static void main(String[] args) {
        	String s = 1 + 3;
        	new Sample().test(s);
        	s += 3;
        	new Sample().test(s);

        	Object a = new Sample();
        	a.test(3);
    	}
	}

Needless to say this program is an invalid Java program.  However, you can
still compile it and run it with jTypeless.

Steps:

1. Compiler: `ant clean dist`

2. Compile Sample.java and run it

    cd examples; ./run-sample.sh

Note that `run-sample.sh` is just:

	#!/bin/sh

	echo Compiling Sample.java
	java -cp $JAVA_HOME/lib/tools.jar:../typeless.jar \
     	com.sun.tools.javac.Main -d . Sample.java

	echo Running Sample now
	java -cp ../typeless.jar:. Sample

Unfortunately, javac script misses up with classpath for now, and creates
a bit of ClassLoader conflicts for now.  So for now, the `run-sample.sh`
invokes the compiler Main class directly

3. Verify the results:

	Compiling Sample.java
	About to transform: /var/tmp/Sample14217.groovy
	Running Sample now
	Int: 4
	Int: 7
	Int: 3

NOTE: This only works with Java 6, not Java 7
