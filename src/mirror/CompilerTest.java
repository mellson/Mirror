package mirror;

import java.lang.reflect.Method;
import java.util.ArrayList;

import compiler.DynamicCompiler;

public class CompilerTest {
	private DynamicCompiler compiler;
	
	public CompilerTest(String source, String className, String methodName, int[] numbers) throws Exception {
		compiler = new DynamicCompiler();
		compiler.init();
		Class<?> c = compiler.compileToClass(className, source);
		Object object = c.newInstance();
		
//		int[] numbers = {10,2,3,4,5};
		
		ArrayList<Class> parameters = new ArrayList<Class>();
		parameters.add(numbers.getClass());
		
		for (Method m : c.getMethods()) {
			System.out.println(m.getParameterTypes());
		}
		
//		Method method = c.getMethod(methodName, parameters);
//		method.invoke(object, parameters);
		
		
		
		// Husk hvis metoden er static, så er object null
		// Object returnValue = method.invoke(null, "parameter-value1");
	}	
}
