package mirror

import compiler.DynamicCompiler
import java.lang.Class

class MirrorCompiler(source: String, className: String, methodName: String, parameters: Array[Object]) {
  // Create a dynamic compiler and get it ready
  val compiler = new DynamicCompiler
  compiler init
  val c = compiler.compileToClass(className, source)
  
  // Create an object
  val o = c.newInstance
  
  // Get the correct method
  val m = c.getMethods find { x => x.getName == methodName }
  val method = m.get
  
  // Invoke the method on the object
  method.invoke(o, parameters); 
}