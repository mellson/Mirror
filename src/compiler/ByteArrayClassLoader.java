/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.util.HashMap;
import java.util.Map;

public class ByteArrayClassLoader extends ClassLoader {
	private Map<String, ByteArrayJavaFileObject> cache = new HashMap<String, ByteArrayJavaFileObject>();

	public ByteArrayClassLoader() throws Exception {
		super(ByteArrayClassLoader.class.getClassLoader());
	}

	public void put(String name, ByteArrayJavaFileObject obj) {
		ByteArrayJavaFileObject co = cache.get(name);
		if (co == null) {
			cache.put(name, obj);
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> cls = null;

		try {
			ByteArrayJavaFileObject co = cache.get(name);
			if (co != null) {
				byte[] ba = co.getClassBytes();
				cls = defineClass(name, ba, 0, ba.length);
			}
		} catch (Exception ex) {
			throw new ClassNotFoundException("Class name: " + name, ex);
		}

		// System.out.printf("Method findClass() called for class %s\n", name);

		return cls;
	}
}
