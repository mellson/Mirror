/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Device
import org.eclipse.swt.widgets.Display

class MirrorView extends ViewPart {
	val ID = "mirror.views.MirrorView"

	def createPartControl(parent: Composite): Unit = {
	  val group = new Composite(parent, SWT.INHERIT_DEFAULT)
	  val white = Display.getDefault.getSystemColor(SWT.COLOR_WHITE) 
			group setBackground(white)
//			val text = new StyledText(group, SWT.NONE)
//			text.pack
//			text.setLocation(10, 10)
//			text setEditable false
//			group.pack
			
			val partListener = new PartListener
			partListener.group = group
//			partListener.text = text
			
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