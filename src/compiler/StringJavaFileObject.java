/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009
 * Modified by Anders Bech Mellson 23.07.12 
 */

package compiler;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;

public class StringJavaFileObject extends SimpleJavaFileObject {
	private String source;

	public StringJavaFileObject(String name, String source) {
		super(URI.create("string:///" + name.replace('.', '/')
				+ Kind.SOURCE.extension), Kind.SOURCE);
		this.source = source;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return this.source;
	}
}
