Typeless  -  Java, but actually Groovy
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
