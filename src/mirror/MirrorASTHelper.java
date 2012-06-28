package mirror;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.MethodInvocation;


public class MirrorASTHelper {	
	@SuppressWarnings("unchecked")
	public static void argAdder(MethodInvocation me, Object o, Object name) {
		List<Object> list = new ArrayList<Object>();
		list.add(o);
		list.add(name);
		me.arguments().addAll(list);
	}
	
	public static String readFile( String file ) throws IOException {
		// Create the file needed for communication
		File mirrorFile = new File(file);
		if (!mirrorFile.exists())
			mirrorFile.createNewFile();
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");
	    
	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    return stringBuilder.toString();
	}
}
