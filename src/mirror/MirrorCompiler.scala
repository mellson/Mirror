package mirror

import compiler.DynamicCompiler

class MirrorCompiler() {
  def compile(source: String, className: String, methodName: String, parameters: Array[Object]) {
    // Create a dynamic compiler and get it ready
    val compiler = new DynamicCompiler
    compiler init
    
    val c = compiler.compileToClass(className, source)

    // Create an object
    val o = c.newInstance

    // Get the correct method (both public and private)
    val m = c.getDeclaredMethods find { x => x.getName == methodName }
    val method = m.get
    
    // Only compile if the correct amount of parameters are given
    if (method.getParameterTypes.length == parameters.length) {
      // If the method was private override that accessibility
      method.setAccessible(true)
      
      // Invoke the method on the object - parameters needs to be mapped to support varargs like behavior
      val returnValue = method.invoke(o, parameters.map(_.asInstanceOf[Object]): _*)
      
      System.out.println("Return value is "+returnValue)
    }
  }
}