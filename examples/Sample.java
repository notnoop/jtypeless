
class Sample {
    private void test(Integer a) { System.out.println("Int: " + a); }

    public static void main(String[] args) {
        String s = 1 + 3;
	new Sample().test(s);
	s += 3;
	new Sample().test(s);

	Object a = new Sample();
	a.test("m");
    }
}
