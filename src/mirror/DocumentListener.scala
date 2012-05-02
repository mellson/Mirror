/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.jface.text.{IDocumentListener,DocumentEvent,IDocument}
import org.eclipse.swt.custom.StyledText

class DocumentListener extends IDocumentListener{
	var document: IDocument = null
	var text: StyledText = null
  
	// React to changes in the source code from the editor
	def documentAboutToBeChanged(event:DocumentEvent) = {}
	def documentChanged(event:DocumentEvent) = { update }
	
	def update = text setText document.get
	
	def dispose(): Unit = {
		document.removeDocumentListener(this)
	}
}