package mirror;

import java.lang.reflect.Array;

public class GenInput {
	public GenInput() {
		Object o = Array.newInstance(int.class, 3);
		int[] a = {1,2,3,4,5};
		Test(a);
	}
	
	public void Test(int[] numbers) {
		for (int number : numbers) {
			System.out.println(number);
		}
	}
}
