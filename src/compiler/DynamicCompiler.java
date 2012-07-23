/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import mirror.DocumentListener;

public class DynamicCompiler {
	private JavaCompiler compiler;
	private DiagnosticCollector<JavaFileObject> collector;
	private JavaFileManager manager;
	public DocumentListener documentListener;

	public void init() throws Exception {
		compiler = ToolProvider.getSystemJavaCompiler();
		collector = new DiagnosticCollector<JavaFileObject>();
		manager = new DynamicClassFileManager<JavaFileManager>(
				compiler.getStandardFileManager(null, null, null));
	}

	public Class<?> compileToClass(String fullName, String javaCode)
			throws Exception {
		Class<?> clazz = null;
		javaCode = addHelperCode(javaCode);
		StringJavaFileObject strFile = new StringJavaFileObject(fullName,
				javaCode);
		Iterable<? extends JavaFileObject> units = Arrays.asList(strFile);
		CompilationTask task = compiler.getTask(null, manager, collector, null,
				null, units);
		boolean status = task.call();
		if (status) {
			System.out.printf("Compilation successful!!!\n");
			clazz = manager.getClassLoader(null).loadClass(fullName);
			documentListener.clearErrorMessage();
		} else {
			for (Diagnostic<?> d : collector.getDiagnostics()) {
				System.out.printf(d.getMessage(null));
				documentListener.clearReturnMessage();
				documentListener.setErrorMessage(d.getMessage(null));
			}
			System.out.printf("***** Compilation failed!!!\n");
		}

		return clazz;
	}

	// Helper source code needed in every class to represent the values
	private String addHelperCode(String source) {
		String sourceCode = "public void stringRepresentation(Object s, String name) {\n"
				+ "        try {\n"
				+ "            PrintWriter out = new PrintWriter(name);\n"
				+ "            if (s.getClass().isArray())\n"
				+ "                out.println(arrayDecorator(s));\n"
				+ "            else\n"
				+ "                out.println(s);\n"
				+ "            out.close();\n"
				+ "        } catch (FileNotFoundException e) {\n"
				+ "        }\n"
				+ "    }\n"
				+ "\n"
				+ "    public static String arrayDecorator(Object a) {\n"
				+ "        String decoratedString = \"[ \";\n"
				+ "        if (int[].class==a.getClass()) {\n"
				+ "            int[] b = (int[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (double[].class==a.getClass()) {\n"
				+ "            double[] b = (double[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (float[].class==a.getClass()) {\n"
				+ "            float[] b = (float[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (byte[].class==a.getClass()) {\n"
				+ "            byte[] b = (byte[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (short[].class==a.getClass()) {\n"
				+ "            short[] b = (short[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (long[].class==a.getClass()) {\n"
				+ "            long[] b = (long[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (char[].class==a.getClass()) {\n"
				+ "            char[] b = (char[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (String[].class==a.getClass()) {\n"
				+ "            String[] b = (String[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        if (Boolean[].class==a.getClass()) {\n"
				+ "            Boolean[] b = (Boolean[])a;\n"
				+ "            for (int i = 0; i < b.length; i++) {\n"
				+ "                String separator = i < b.length - 1 ? \" ; \" : \"\";\n"
				+ "                decoratedString += b[i] + separator;\n"
				+ "            }\n"
				+ "        }\n"
				+ "        decoratedString += \" ]\";\n"
				+ "        return decoratedString;\n" + "    }\n";

		String importSource = "\n" + "import java.io.FileNotFoundException;\n"
				+ "import java.io.PrintWriter;\n";
		// Add the imports needed for the above source code to work
		int i = 0, index = 0;
		for (String s : source.split("package.*?;")) {
			if (i < 2) {
				i++;
				index = source.indexOf(s);
			} else
				break;
		}
		source = source.substring(0, index) + importSource
				+ source.substring(index);

		// Add the extra source code before the last }
		index = source.lastIndexOf('}');
		source = source.substring(0, index) + sourceCode
				+ source.substring(index);
		return source;
	}
}
