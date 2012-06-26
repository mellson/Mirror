package mirror

import compiler.DynamicCompiler
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AST
import sun.tools.javac.ErrorMessage

class MirrorCompiler() {
  var documentListener: DocumentListener = null;
  
  def compile(source: String, className: String, methodName: String, parameters: Array[Object], unit: ICompilationUnit) {
    // Create a dynamic compiler and get it ready
    val compiler = new DynamicCompiler
    compiler.documentListener = documentListener
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
//      val returnValue = method.invoke(o, parameters.map(_.asInstanceOf[Object]): _*)

//      System.out.println("Return value is " + returnValue)
    }
    startParsing(unit, methodName)
  }
  
  def startParsing(unit: ICompilationUnit, methodName: String) = {
    val parser = parse(unit)
    val visitor = new MirrorMethodVisitor
    visitor.methodName = methodName
    parser.accept(visitor)
  }
  
  def parse(unit: ICompilationUnit) : CompilationUnit = {
		val parser = ASTParser.newParser(AST.JLS3)
		parser.setKind(ASTParser.K_COMPILATION_UNIT)
		parser.setSource(unit)
		parser.setResolveBindings(true)
		parser.createAST(null).asInstanceOf[CompilationUnit] // parse
	}
}