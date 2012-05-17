/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.io.*;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;

public class DynamicClassFileManager <FileManager> extends ForwardingJavaFileManager<JavaFileManager> {
	private ByteArrayClassLoader loader = null;
	
	DynamicClassFileManager(StandardJavaFileManager mgr) {
		super(mgr);
		try {
			loader = new ByteArrayClassLoader();
		}
		catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String name, Kind kind, FileObject sibling)
		throws IOException {
		ByteArrayJavaFileObject co = new ByteArrayJavaFileObject(name, kind);
		loader.put(name, co);
		return co;
	}
	
	@Override
	public ClassLoader getClassLoader(Location location) {
        return loader;
	}
}
