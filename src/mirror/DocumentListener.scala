package mirror

import org.eclipse.jface.text.IDocumentListener
import org.eclipse.jface.text.DocumentEvent
import org.eclipse.swt.custom.StyledText
import org.eclipse.jface.text.IDocument

class DocumentListener extends IDocumentListener{
	var document: IDocument = null
	var text: StyledText = null
  
	def documentAboutToBeChanged(event:DocumentEvent) {}
	def documentChanged(event:DocumentEvent) {
					text setText document.get
					}
}

object DocumentListener extends DocumentListener {}