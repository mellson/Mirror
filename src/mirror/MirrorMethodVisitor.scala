package mirror

import org.eclipse.jdt.core.dom.ASTVisitor
import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.ReturnStatement

class MirrorMethodVisitor extends ASTVisitor {
  var methodName: String = null
  val methods = new ArrayBuffer[String]

  override def visit(node: MethodDeclaration) = {
    if (node.getName.toString.equals(methodName))
      for (s <- node.getBody.statements.toArray) {
        if (s.isInstanceOf[VariableDeclarationStatement])
          println("Var Decl: " + s)
        else if (s.isInstanceOf[ExpressionStatement])
          println("Expr Stmt: " + s)
        else if (s.isInstanceOf[ReturnStatement])
          println("Return Stmt: " + s)
      }
    methods += node.getName.toString
    super.visit(node)
  }
}