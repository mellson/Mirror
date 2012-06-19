/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IWorkbenchPage

class MirrorView extends ViewPart {
  val ID = "mirror.views.MirrorView"
  var group: Composite = null
  val inputHandler = new InputHandler
  
  def createPartControl(parent: Composite): Unit = {
    // Create group view for the plugin & set it's background to white
    group = new Composite(parent, SWT.INHERIT_DEFAULT)
    val white = Display.getDefault.getSystemColor(SWT.COLOR_WHITE)
    group setBackground(white)
  }

  // Method called whenever the view gets focused
  def setFocus(): Unit = {
    // Create a partListener
    val partListener = new PartListener
    
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

  def log(s:String) = Activator.out.println(s)
}