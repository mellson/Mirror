package mirror.views

import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.custom.StyledText

class MirrorViewScala extends ViewPart {
	val ID = "mirror.views.MirrorViewScala"
	var text: StyledText = null

	def createPartControl (parent : Composite) : Unit = {
	  text = new StyledText(parent, SWT.BORDER)
	  text setText("Scala kode rocks")
	}

	def setFocus(): Unit = {
	  text setText((text getText) + ".")
	}
}

object MirrorViewScala extends MirrorViewScala { }