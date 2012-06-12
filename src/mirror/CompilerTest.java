package mirror;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compiler.DynamicCompiler;

public class CompilerTest {
	private DynamicCompiler compiler;
	
	public CompilerTest(String source, String className, String methodName) throws Exception {
		compiler = new DynamicCompiler();
		compiler.init();
		Class<?> c = compiler.compileToClass(className, source);
		Object object = c.newInstance();
		
		int[] numbers = randomNumberArray(10);
		
		Method method = c.getMethod(methodName, numbers.getClass());
		method.invoke(object, numbers);
	}
	
	private int[] randomNumberArray(int size) {
		// Create an ordered list
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < size; i++) {
		    list.add(i);
		}

		// Shuffle it
		Collections.shuffle(list);

		// Get an int[] array
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
		    array[i] = list.get(i);
		}
		
		return array;
	}
}
