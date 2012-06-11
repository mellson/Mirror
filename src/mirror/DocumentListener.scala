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

class DocumentListener extends IDocumentListener{
	var document: IDocument = null
	var text: StyledText = null
	var compiler: DynamicCompiler = null
	var editor: IEditorPart = null
  
	// React to changes in the source code from the editor
	def documentAboutToBeChanged(event:DocumentEvent) = {}
	def documentChanged(event:DocumentEvent) = { compile }
	
	def update = text setText document.get
	
	def dispose(): Unit = {
		document.removeDocumentListener(this)
	}
	
	// Get the current caret position in the source file
	def caretPosition = editor.getAdapter(classOf[Control]).asInstanceOf[StyledText].getCaretOffset
	
	def printMethodName = {
	  val unit = document.get.asInstanceOf[ICompilationUnit]
	  val types = unit.getAllTypes
	  for (itype : IType <- types) {
	    val methods = itype.getMethods
	    for (method : IMethod <- methods) {
	      if (caretPosition >= method.getSourceRange.getOffset && caretPosition <= method.getSourceRange.getLength + method.getSourceRange.getOffset) {
	        System.out.println(method.getElementName);
	      }
	    }
	  }
	}
	
//	def packageName = {
//	  
//	}
	
	
	
	def compile = {
//	  compiler = new DynamicCompiler
//	  compiler.init
//	  val c = compiler.compileToClass("a", document.get).newInstance
//	  val c: CompilerTest = new CompilerTest(document.get)
	  System.out.println("Caret position "+caretPosition)
//	  printMethodName
	}
}