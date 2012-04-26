package mirror.views

import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.custom.StyledText
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI;

class MirrorViewScala extends ViewPart {
	val ID = "mirror.views.MirrorViewScala"
	var text: StyledText = null
	var activeEditor: IEditorPart = null
	
	def createPartControl (parent : Composite) : Unit = {
	  text = new StyledText(parent, SWT.BORDER)
	  text setEditable(false)
	  
	  activeEditor = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.getActiveEditor

	  if (activeEditor != null)
		  text setText(activeEditor.getEditorInput.getName)
	  else
		  text setText("Null")
	}

	def setFocus(): Unit = {
	  text setText((text getText) + ".")
	}
}

object MirrorViewScala extends MirrorViewScala { }