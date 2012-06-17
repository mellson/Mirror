/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.jface.text.{IDocumentListener,DocumentEvent,IDocument}
import org.eclipse.jdt.core.{ICompilationUnit,IType,IMethod}
import org.eclipse.swt.custom.StyledText
import compiler.DynamicCompiler
import org.eclipse.swt.widgets.Control
import org.eclipse.ui.IEditorPart
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.internal.core.CreateCompilationUnitOperation
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.internal.ui.javaeditor.ICompilationUnitDocumentProvider
import org.eclipse.jdt.ui.JavaUI
import java.lang.reflect.Constructor
import org.eclipse.jdt.core.Signature
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Layout
import scala.collection.mutable.ArrayBuffer
import org.eclipse.swt.widgets.Composite

class DocumentListener extends IDocumentListener {
	var document: IDocument = null
	var text: StyledText = null
	var group: Composite = null
	var compiler: DynamicCompiler = null
	var editor: IEditorPart = null
	var unit: ICompilationUnit = null
	var packageName: String = null
	var className: String = null
	
	// React to changes in the source code from the editor
	def documentAboutToBeChanged(event:DocumentEvent) = {}
	
	def documentChanged(event:DocumentEvent) = {
	  compile
	  }
	
	def update =  {
	  if (methodName!=null) {
	    // Dispose old text in the group view
	    for (child <- group.getChildren)
	      child.dispose
//	    group setText methodName
	    var y = 0
	    for (input <- getMethodInputs(unit)) {
	    	val text = new StyledText(group, SWT.NONE)
	    	text setEditable false
	    	text setLocation(0, y)
	      	text setText input + " = "
	    	y += text.getLineHeight
	    	text pack
	    }
	  }
		  
//	  getMethod(unit)
	}
	
	def dispose(): Unit = {
		document.removeDocumentListener(this)
	}
	
	// Get the current caret position in the source file
	def caretPosition = editor.getAdapter(classOf[Control]).asInstanceOf[StyledText].getCaretOffset
	
	def getMethodInputs(unit: ICompilationUnit) = {
	  val inputs = new ArrayBuffer[String]
	  val allTypes = unit.getAllTypes
	
      for (itype <- allTypes) {
        val methods = itype.getMethods
        for (method <- methods) {
          if (method.getElementName.equals(methodName)) {
          val parameters = method.getParameters
          for (parameter <- parameters) {
            inputs += Signature.toString(parameter.getTypeSignature) + " " + parameter.getElementName
            }
          }
        }
      }
	  inputs
    }
	
	def methodName = {
	  val types = unit.getAllTypes
	  var name: String = null;
	  for (itype : IType <- types) {
	    val methods = itype.getMethods
	    for (method <- methods) {
	      if (caretPosition >= method.getSourceRange.getOffset && caretPosition <= method.getSourceRange.getLength + method.getSourceRange.getOffset) {
	        name = method.getElementName
	      }
	    }
	  }
	  name
	}
	
//	def packageName = {
//	  
//	}
	
	
	
	def compile = {
//	  compiler = new DynamicCompiler
//	  compiler.init
//	  val c = compiler.compileToClass("a", document.get).newInstance
	  val c = new CompilerTest(document.get,(packageName+"."+className),methodName)
//	  System.out.println("Caret position "+caretPosition)
	}
}