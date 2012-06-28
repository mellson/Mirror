package mirror

import org.eclipse.jdt.core.dom.ASTVisitor
import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.Type

class MirrorMethodVisitor extends ASTVisitor {
  var methodName: String = null
  val declarations = new ArrayBuffer[VariableDeclarationStatement]

  override def visit(node: MethodDeclaration) = {
    if (node.getName.toString.equals(methodName)) {
      val ast = node.getAST
      for (s <- node.getBody.statements.toArray) {
        if (s.isInstanceOf[VariableDeclarationStatement]) {
          val x = s.asInstanceOf[VariableDeclarationStatement]
          declarations += x          
//          x.getType
        } else if (s.isInstanceOf[ExpressionStatement]) {}
        //          println("Expr Stmt: " + s)
        else if (s.isInstanceOf[ReturnStatement]) {}
        //          println("Return Stmt: " + s)
      }
    }
    super.visit(node)
  }
}