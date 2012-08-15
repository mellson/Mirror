/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.dom._

class MirrorMethodVisitor extends ASTVisitor {
  var methodName: String = null
  val declarations = new ArrayBuffer[Statement]

  override def visit(node: MethodDeclaration) = {
    if (node.getName.toString.equals(methodName)) {
      for (s <- node.getBody.statements.toArray) {
        if (s.isInstanceOf[VariableDeclarationStatement]) {
          val x = s.asInstanceOf[VariableDeclarationStatement]
          declarations += x
        } else if (s.isInstanceOf[ExpressionStatement]) {
          // Disabling more statements for now.
//          val x = s.asInstanceOf[ExpressionStatement]
//          declarations += x
        }
      }
    }
    super.visit(node)
  }
}