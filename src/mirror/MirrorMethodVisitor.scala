package mirror

import org.eclipse.jdt.core.dom.ASTVisitor
import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.ExpressionStatement

class MirrorMethodVisitor extends ASTVisitor {
  val methods = new ArrayBuffer[String]
  
  override def visit(node: MethodDeclaration) = {
    if (node.getName.toString.equals("sort"))
      for(s<-node.getBody.statements.toArray)
        if (s.isInstanceOf[ExpressionStatement])
          println(s)
    methods += node.getName.toString
    super.visit(node)
  }
}