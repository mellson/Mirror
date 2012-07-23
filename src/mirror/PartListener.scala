package mirror

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jdt.core._
import org.eclipse.swt.custom._
import org.eclipse.swt.widgets._
import org.eclipse.ui._
import org.eclipse.ui.texteditor.ITextEditor

class PartListener extends IPartListener2 with CaretListener {
  var group: Composite = null
  var inputHandler: InputHandler = null
  var caretOffset = 0
  val listener: DocumentListener = new DocumentListener

  def compilationUnitForDocument = {
    // Get the root of the workspace
    val workspace = ResourcesPlugin.getWorkspace
    val root = workspace.getRoot
    // Get all projects in the workspace
    val projects = root.getProjects

    var compilationUnit: ICompilationUnit = null
    var packageName: String = null
    var className: String = null

    // Loop over all projects
    for (project <- projects) {
      // Check if we have a Java project
      if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
        val javaProject = JavaCore.create(project)
        val packages = javaProject.getPackageFragments
        for (mypackage <- packages) {
          // Check if it is a source file
          if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
            for (unit <- mypackage.getCompilationUnits) {
              val page = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage
              val activeEditor = page.getActiveEditor
              // Check if the source code is in the active editor
              if (unit.getElementName().equals(activeEditor.getTitle())) {
                packageName = mypackage.getElementName
                compilationUnit = unit
                // Remove .java from the unit to get the classname
                className = unit.getElementName.substring(0, (unit.getElementName.length - 5))
              }
            }
          }
        }
      }
    }
    (compilationUnit, packageName, className)
  }

  def partActivated(partRef: IWorkbenchPartReference): Unit = {
    // Check if the part activated is a Java source file
    val title = partRef.getTitle.toLowerCase
    if (title.endsWith(".java")) {
      // Get the current editor
      val activeEditor = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage.getActiveEditor
      if (activeEditor.isInstanceOf[ITextEditor]) {

        // Get the source code from the editor
        val document = activeEditor.asInstanceOf[ITextEditor].getDocumentProvider.getDocument(activeEditor.getEditorInput)

        // Listen to caret events and update after an event
        activeEditor.getAdapter(classOf[Control]).asInstanceOf[StyledText].addCaretListener(this)

        // Give the listener the correct references
        listener.document = document
        listener.group = group
        listener.editor = activeEditor
        val x = compilationUnitForDocument
        listener.unit = x._1
        listener.packageName = x._2
        listener.className = x._3
        listener.inputHandler = inputHandler

        // React to changes in the source
        document.addDocumentListener(listener)

        // Update the view with the currently loaded source code
        listener.update
      }
    }
  }

  def partClosed(partRef: IWorkbenchPartReference): Unit = {
    // Check if the part closed is a Java source file
    val title = partRef.getTitle.toLowerCase
    if (title.endsWith(".java")) {
      // Don't listen to changes in the document anymore
      listener.dispose
    }
  }

  def caretMoved(event: CaretEvent) {
    if (event.caretOffset != caretOffset) {
      listener.caretPosition = event.caretOffset
      listener.update
      caretOffset = event.caretOffset
    }
  }

  // Unused methods inherited from interface
  def partBroughtToTop(partRef: IWorkbenchPartReference): Unit = {}
  def partDeactivated(partRef: IWorkbenchPartReference): Unit = {}
  def partOpened(partRef: IWorkbenchPartReference): Unit = {}
  def partHidden(partRef: IWorkbenchPartReference): Unit = {}
  def partVisible(partRef: IWorkbenchPartReference): Unit = {}
  def partInputChanged(partRef: IWorkbenchPartReference): Unit = {}
}