/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class ByteArrayJavaFileObject extends SimpleJavaFileObject {
	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public ByteArrayJavaFileObject(String name, Kind kind) {
		super(
				URI.create("string:///" + name.replace('.', '/')
						+ kind.extension), kind);
	}

	public byte[] getClassBytes() {
		return bos.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
}
