package mirror

import compiler.DynamicCompiler
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.dom._
import org.eclipse.jface.text.Document
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.util.ArrayList
import java.util.Collection
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite

class MirrorCompiler() {
  var documentListener: DocumentListener = null
  var modifiedSource: Document = null;
  var varDeclStmts: Array[VariableDeclarationStatement] = null
  var parser: CompilationUnit  = null

  def compile(source: String, className: String, methodName: String, parameters: Array[Object], unit: ICompilationUnit) {
    // Parse the AST and get the variables
    startParsing(unit, methodName)
    
    // Add the code needed for retrieving values
    rewrite(unit)

    // Create a dynamic compiler and get it ready
    val compiler = new DynamicCompiler
    compiler.documentListener = documentListener
    compiler init

    val c = compiler.compileToClass(className, modifiedSource.get)

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

      if (returnValue != null)
        documentListener.setReturnMessage(returnValue.toString)

      documentListener.update
    }
  }

  def rewrite(unit: ICompilationUnit) {
//    parser = parse(unit)
    val ast = parser.getAST
    val rewriter = ASTRewrite.create(ast)
    
    // Get insertion position
    val typeDecl = parser.types().get(0).asInstanceOf[TypeDeclaration]
    for (m <- typeDecl.getMethods()) {
      if (m.getName.toString.equals(documentListener.methodName)) {
        // Which line in the block should the extra calls be inserted at?
        var i = 1
        for (v <- varDeclStmts) {
          val block = m.getBody
          
          // create new statements for insertion
          val newInvocation = ast.newMethodInvocation
          newInvocation.setName(ast.newSimpleName("anders"))

          // Get the object and the name of the object and add that as arguments to the methodcall
          val name = v.fragments.get(0).asInstanceOf[VariableDeclarationFragment].getName.toString
          val nameSL = ast.newStringLiteral
          nameSL.setLiteralValue(name)
          // This bit needs to be done in Java because Scala doesn't like the type uncertainty
          MirrorASTHelper.argAdder(newInvocation, ast.newSimpleName(name), nameSL)
          val newStatement = ast.newExpressionStatement(newInvocation)
          
          // Insert the new code and apply the edits
          val listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY)
          listRewrite.insertAt(newStatement, i, null)
          val edits = rewriter.rewriteAST()
          modifiedSource = new Document(unit.getSource())
          edits.apply(modifiedSource)
          
          // Adding two lines for the next insert because I just inserted a line here
          i += 2
        }
      }
    }
  }

  def startParsing(unit: ICompilationUnit, methodName: String) = {
    parser = parse(unit)
    val visitor = new MirrorMethodVisitor
    visitor.methodName = methodName
    parser.accept(visitor)
    documentListener.ar = visitor.declarations.toArray
    varDeclStmts = visitor.declarations.toArray
  }

  def parse(unit: ICompilationUnit): CompilationUnit = {
    val parser = ASTParser.newParser(AST.JLS3)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setSource(unit)
    parser.setResolveBindings(true)
    parser.createAST(null).asInstanceOf[CompilationUnit] // parse
  }
}