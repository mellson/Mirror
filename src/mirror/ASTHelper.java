package mirror;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.MethodInvocation;

// Helper class for handling files and adding uncertain types to method invocations
public class ASTHelper {
	@SuppressWarnings("unchecked")
	public static void argAdder(MethodInvocation me, Object o, Object name) {
		List<Object> list = new ArrayList<Object>();
		list.add(o);
		list.add(name);
		me.arguments().addAll(list);
	}

	public static String readFile(String file) throws IOException {
		// Create the file needed for communication
		File mirrorFile = new File(file);
		if (!mirrorFile.exists())
			mirrorFile.createNewFile();
		mirrorFile.deleteOnExit();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		return stringBuilder.toString();
	}
}
