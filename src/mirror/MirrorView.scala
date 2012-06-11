/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI
import org.eclipse.jface.text.{IDocument, IDocumentListener}
import org.eclipse.ui.texteditor.ITextEditor

class MirrorView extends ViewPart {
	val ID = "mirror.views.MirrorView"
			val listener: DocumentListener = new DocumentListener

			def createPartControl(parent: Composite): Unit = {
			// Get the current editor
			val activeEditor = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.getActiveEditor
					if (activeEditor.isInstanceOf[ITextEditor]) {
						val text = new StyledText(parent, SWT.BORDER)
						text setEditable false

						PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.getActiveEditor
						
						// Get the source code from the editor
						val document = activeEditor.asInstanceOf[ITextEditor].getDocumentProvider.getDocument(activeEditor.getEditorInput)
						
						// Give the listener the correct references
						listener.document = document
						listener.text = text
						listener.editor = activeEditor

						// React to changes in the source			
						document.addDocumentListener(listener)

						// Update the view with the currently loaded source code
						listener.update
					} else
						log("Missed")
	}
	
	// Method called whenever the view gets focused
	def setFocus(): Unit = { System.out.println("View got focused") }

	// Removing the listener when the plug-in gets disposed
	override def dispose(): Unit = {
			listener.dispose
			super.dispose
	}

	def log(s:String) = Activator.out.println(s)
}