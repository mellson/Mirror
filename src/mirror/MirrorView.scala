package mirror

import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI
import java.beans.PropertyChangeListener
import org.eclipse.ui.IPropertyListener
import org.eclipse.swt.events.KeyListener
import org.eclipse.swt.events.KeyEvent
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor
import org.eclipse.swt.widgets.Control
import org.eclipse.jface.text.IDocument
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.jface.text.DocumentEvent

class MirrorView extends ViewPart {
	val ID = "mirror.views.MirrorView"
	var text: StyledText = null
	var activeEditor: IEditorPart = null
	def createPartControl(parent: Composite): Unit = {
		text = new StyledText(parent, SWT.BORDER)
		text setEditable false
		activeEditor = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.getActiveEditor
		if (activeEditor != null) {
			val document: IDocument = activeEditor.asInstanceOf[ITextEditor].getDocumentProvider.getDocument(activeEditor.getEditorInput)
			document.addDocumentListener(new IDocumentListener {
				def documentAboutToBeChanged(event:DocumentEvent) {}
				def documentChanged(event:DocumentEvent) {
					text setText document.get
					}
				})
			}
		}

	def setFocus(): Unit = { }
}

object MirrorView extends MirrorView { }