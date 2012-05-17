/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.util.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

public class DynamicCompiler {
	private JavaCompiler compiler;
	private DiagnosticCollector<JavaFileObject> collector;
	private JavaFileManager manager;
		
	public void init()
		throws Exception {
		compiler = ToolProvider.getSystemJavaCompiler();
		collector = new DiagnosticCollector<JavaFileObject>();
		manager = new DynamicClassFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null));
	}
	
	public Class<?> compileToClass(String fullName, String javaCode)
		throws Exception {
		Class<?> clazz = null;
		
		StringJavaFileObject strFile = new StringJavaFileObject(fullName, javaCode);
		Iterable<? extends JavaFileObject> units = Arrays.asList(strFile);
		CompilationTask task = compiler.getTask(null, manager, collector, null, null, units);
		boolean status = task.call();
		if (status) {
			System.out.printf("Compilation successful!!!\n");
			clazz = manager.getClassLoader(null).loadClass(fullName);
		}
		else {
			System.out.printf("Message:\n");
			for (Diagnostic<?> d : collector.getDiagnostics()) {
				System.out.printf("%s\n", d.getMessage(null));
			}
			System.out.printf("***** Compilation failed!!!\n");
		}
		
		return clazz;
	}
}
