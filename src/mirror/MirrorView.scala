/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI

class MirrorView extends ViewPart {
	val ID = "mirror.views.MirrorView"

	def createPartControl(parent: Composite): Unit = {
			val text = new StyledText(parent, SWT.BORDER)
			text setEditable false
			
			val partListener = new PartListener
			partListener.text = text
			
			// Listen to the workbench for open and close events
			PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.addPartListener(partListener)
	}

	// Method called whenever the view gets focused
	def setFocus(): Unit = {}

	// Removing the listener when the plug-in gets disposed
	override def dispose(): Unit = {
			super.dispose
	}

	def log(s:String) = Activator.out.println(s)
}