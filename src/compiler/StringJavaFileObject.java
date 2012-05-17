
/*
 * Name: Bhaskar S
 * 
 * Date: 10/10/2009 
 */

package compiler;

import java.net.*;
import javax.tools.*;

public class StringJavaFileObject extends SimpleJavaFileObject {
	private String source;
	
	public StringJavaFileObject(String name, String source) {
		super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.source = source;
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return this.source;
	}
}
