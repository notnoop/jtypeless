jTypeless  -  Java, but actually Groovy
------------------------------------

This sample project is inspired by
[typelessj](http://code.google.com/p/typelessj/) work, by
Michael Bayne and Michael Ernst.

This is a proof-of-concept implementation that treats Java code like
a Groovy code (with taking care of some edge cases), with ignoring all the
types!

This sample code illustrates few points:

1. You can use an annotation processor to invoke a different specialized
  (dynamic?) compiler, without affecting the usability of the tool

2. how you can compile Groovy but ensure the code is syntactically valid
   Java code

3. how you can generate Groovy transformations (not done yet)

Unfortunately, there are some rough edges here, but hey, this worked within
less than two hours of work.


Sample:

Consider the following class:

[Available in jtypeless/examples/Sample.java]
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

    cd examples
    javac -cp ../jtypeless.jar Sample.java
    java -cp ../jtypeless.jar:. Sample


3. Verify the results:

	Int: 4
	Int: 7
	Int: 3

NOTE: This only works with Java 6, not Java 7
