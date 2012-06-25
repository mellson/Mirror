/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.custom.StyledText

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

    // Check if the editor is onscreen - if it's not create start button
    if (PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage != null)
      setup
    else {
      val startButton = new Button(group, SWT.PUSH);
      startButton.setText("Start MirrorView");
      startButton.addSelectionListener(new SelectionAdapter() {
        override def widgetSelected(e: SelectionEvent): Unit = {
          setup
          startButton.dispose
          val inputLabel = new Label(group, SWT.NONE)
          inputLabel setText "Now focus the editor"
          inputLabel pack
        }
      })
      startButton.pack
    }
  }

  def setup = {
    // Listen to the workbench for open and close events
    PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.addPartListener(partListener)

    // Set the reference to the group view
    partListener.group = group
    partListener.inputHandler = inputHandler
  }

  // Method called whenever the view gets focused
  def setFocus(): Unit = {

  }

  // Removing the listener when the plug-in gets disposed
  override def dispose(): Unit = {
    super.dispose
  }

  def log(s: String) = Activator.out.println(s)
}