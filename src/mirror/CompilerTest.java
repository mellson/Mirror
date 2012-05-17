package mirror;

import java.lang.reflect.Method;

import compiler.DynamicCompiler;

public class CompilerTest {
	private DynamicCompiler compiler;
	
	public CompilerTest(String source) throws Exception {
		compiler = new DynamicCompiler();
		compiler.init();
		Class<?> c = compiler.compileToClass("a.QuickSort", source);
		Object object = c.newInstance();
		int[] numbers = {5,4,3,2,1};
		System.out.println();
		for (int i : numbers) {
			System.out.print(i);
		}
		Method method = c.getMethod("sort", numbers.getClass());
		method.invoke(object, numbers);
	}
}
