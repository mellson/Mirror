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

class DocumentListener extends IDocumentListener {
	var document: IDocument = null
	var text: StyledText = null
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
	  if (methodName!=null)
		  text setText packageName+"."+className
	}
	
	def dispose(): Unit = {
		document.removeDocumentListener(this)
	}
	
	// Get the current caret position in the source file
	def caretPosition = editor.getAdapter(classOf[Control]).asInstanceOf[StyledText].getCaretOffset
	
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