/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.part.ViewPart

class MirrorView extends ViewPart {
  val ID = "mirror.views.MirrorView"
  var group: Composite = null
  val inputHandler = new InputHandler
  val partListener = new PartListener
  
  def createPartControl(parent: Composite): Unit = {
    // Create group view for the plugin & set it's background to white
    group = new Composite(parent, SWT.INHERIT_DEFAULT)
    val white = Display.getDefault.getSystemColor(SWT.COLOR_WHITE)
    group setBackground (white)

    if (PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage != None)
      setFocus
  }

  // Method called whenever the view gets focused
  def setFocus(): Unit = {
    // Listen to the workbench for open and close events
    PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.addPartListener(partListener)

    // Set the reference to the group view
    partListener.group = group
    partListener.inputHandler = inputHandler
  }

  // Removing the listener when the plug-in gets disposed
  override def dispose(): Unit = {
    super.dispose
  }

  def log(s: String) = Activator.out.println(s)
}