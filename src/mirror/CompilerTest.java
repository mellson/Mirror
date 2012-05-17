package mirror;

import java.lang.reflect.Method;

import compiler.DynamicCompiler;

public class CompilerTest {
	private DynamicCompiler compiler;
	
	public CompilerTest(String source) throws Exception {
		compiler = new DynamicCompiler();
		compiler.init();
		Class<?> c = compiler.compileToClass("a.SimpleJava", source);
		Object object = c.newInstance();
		Method method = c.getMethod("test", null);
		method.invoke(object, null);
		
	}
}
