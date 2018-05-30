package ch.rasc.extclassgenerator.test;

public class Test {
	public static void main(String[] args) {
		H(new T() {
			@Override
			public void call() {
				System.out.println(111);
			}
		});
	}

	static void H(T t) {
		t.call();
	}
}
