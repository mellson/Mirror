/* (c) 2012 Anders Bech Mellson - anbh@itu.dk */

package mirror

import scala.collection.mutable.ArrayBuffer
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.Signature
import org.eclipse.jface.text.DocumentEvent
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.ui.IEditorPart
import compiler.DynamicCompiler
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Event

class DocumentListener extends IDocumentListener {
  var document: IDocument = null
  var group: Composite = null
  var compiler: DynamicCompiler = null
  var editor: IEditorPart = null
  var unit: ICompilationUnit = null
  var packageName: String = null
  var className: String = null

  // React to changes in the source code from the editor
  def documentAboutToBeChanged(event:DocumentEvent) = {}
  def documentChanged(event:DocumentEvent) = {
    compile
  }

  def update =  {
    if (methodName!=null) {
      // Dispose old text in the group view
      for (child <- group.getChildren)
        child.dispose
        
      // Set up labels and inputs for each parameter a method takes  
      var y = 0
      for (input <- getMethodInputs(unit)) {
        val inputLabel = new Label(group, SWT.NONE)
        inputLabel setLocation(0, y)
        inputLabel setText input + " = "
        inputLabel pack
        val point = inputLabel getSize
        
        val inputValue = new Text(group, SWT.SINGLE)
        inputValue setLocation(point.x, y)
        inputValue setSize(group.getSize.x - point.x, inputValue.getLineHeight)
        inputValue setMessage "Set the value for " + input + " here"
        inputValue addListener(SWT.KeyDown, new Listener() {
          def handleEvent(event: Event) = {
            // Check when the user presses the enter key
            if (event.keyCode==13) {
              System.out.println(inputValue getText)
            }
          }
        })
        
        y += point.y
      }
    }
  }

  def dispose(): Unit = {
    document.removeDocumentListener(this)
  }

  // Get the current caret position in the source file
  def caretPosition = editor.getAdapter(classOf[Control]).asInstanceOf[StyledText].getCaretOffset

  def getMethodInputs(unit: ICompilationUnit) = {
    val inputs = new ArrayBuffer[String]
    val allTypes = unit.getAllTypes

    for (itype <- allTypes) {
      val methods = itype.getMethods
      for (method <- methods) {
        if (method.getElementName.equals(methodName)) {
          val parameters = method.getParameters
          for (parameter <- parameters) {
            inputs += Signature.toString(parameter.getTypeSignature) + " " + parameter.getElementName
          }
        }
      }
    }
    inputs
  }

  def methodName = {
    val types = unit.getAllTypes
    var name: String = null;
    for (itype : IType <- types) {
      val methods = itype.getMethods
      for (method <- methods) {
        if (caretPosition >= method.getSourceRange.getOffset && caretPosition <= method.getSourceRange.getLength + method.getSourceRange.getOffset) {
          name = method.getElementName
        }
      }
    }
    name
  }

  def compile = {
    val c = new CompilerTest(document.get,(packageName+"."+className),methodName)
  }
}