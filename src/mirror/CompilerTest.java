package mirror;

import java.lang.reflect.Method;

import compiler.DynamicCompiler;

public class CompilerTest {
	private DynamicCompiler compiler;
	
	public CompilerTest(String source, String className, String methodName, int[] numbers) throws Exception {
		compiler = new DynamicCompiler();
		compiler.init();
		Class<?> c = compiler.compileToClass(className, source);
		Object object = c.newInstance();
		
//		int[] numbers = {10,2,3,4,5};
		
		Method method = c.getMethod(methodName, numbers.getClass());
		method.invoke(object, numbers);
	}	
}
