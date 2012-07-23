package mirror

import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.VariableDeclarationStatement

class MirrorMethodVisitor extends ASTVisitor {
  var methodName: String = null
  val declarations = new ArrayBuffer[VariableDeclarationStatement]

  override def visit(node: MethodDeclaration) = {
    if (node.getName.toString.equals(methodName)) {
      for (s <- node.getBody.statements.toArray) {
        if (s.isInstanceOf[VariableDeclarationStatement]) {
          val x = s.asInstanceOf[VariableDeclarationStatement]
          declarations += x
        }
      }
    }
    super.visit(node)
  }
}