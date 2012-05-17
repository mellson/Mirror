/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.jface.text.{IDocumentListener,DocumentEvent,IDocument}
import org.eclipse.swt.custom.StyledText
import compiler.DynamicCompiler

class DocumentListener extends IDocumentListener{
	var document: IDocument = null
	var text: StyledText = null
	var compiler: DynamicCompiler = null
  
	// React to changes in the source code from the editor
	def documentAboutToBeChanged(event:DocumentEvent) = {}
	def documentChanged(event:DocumentEvent) = { compile }
	
	def update = text setText document.get
	
	def dispose(): Unit = {
		document.removeDocumentListener(this)
	}
	
	def compile = {
//	  compiler = new DynamicCompiler
//	  compiler.init
//	  val c = compiler.compileToClass("a", document.get).newInstance
	  val c: CompilerTest = new CompilerTest(document.get)
	}
}