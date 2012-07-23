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
import org.eclipse.swt.events.FocusEvent
import org.eclipse.swt.events.FocusListener
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Text
import org.eclipse.ui.IEditorPart
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import java.io.File

class DocumentListener extends IDocumentListener {
  var document: IDocument = null
  var group: Composite = null
  var inputHandler: InputHandler = null
  val compiler = new MirrorCompiler()
  compiler.documentListener = this
  var editor: IEditorPart = null
  var unit: ICompilationUnit = null
  var packageName: String = null
  var className: String = null
  var parameters: ArrayBuffer[Object] = null
  var errorLabel: Label = null
  var errorMessage = ""
  var returnLabel: Label = null
  var returnMessage = ""
  var y = 0
  var caretPosition = 0
  var ar: Array[VariableDeclarationStatement] = null

  // React to changes in the source code from the editor
  def documentAboutToBeChanged(event: DocumentEvent) = {}
  def documentChanged(event: DocumentEvent) = {
    compile
  }

  def update: Int = {
    if (methodName != null) {
      // Dispose old text in the group view
      for (child <- group.getChildren)
        child.dispose

      parameters = new ArrayBuffer[Object]

      // Set up labels and inputs for each parameter a method takes  
      y = 0
      for (input <- getMethodInputs(unit)) {
        val inputLabel = new Label(group, SWT.NONE)
        inputLabel setLocation (0, y)
        inputLabel setText input + " = "
        inputLabel pack
        val point = inputLabel getSize
        val inputValue = new Text(group, SWT.SINGLE)
        inputValue setLocation (point.x, y)
        inputValue setSize (group.getSize.x - point.x, inputValue.getLineHeight)

        if (inputHandler.allowedInput(input)) {
          // Check if there is saved any values for the input
          var message = inputHandler.savedInputs.get(input + methodName)
          if (message != None) {
            inputValue setMessage message.get.toString
            parameters += inputHandler objectFromString (input, methodName, message.get.toString)
          } else {
            val x = inputHandler randomObjectFromString(input, methodName)
            inputValue setMessage x._2
            parameters += x._1
            /* Userinput welcome below disabled in favor of random generated input
            inputValue setMessage "Set the value for " + input + " here"
            if (inputHandler.isInputArray(input))
              inputValue setMessage inputValue.getMessage + " (use ; as separater)" */
          }

          // When the textfield gets focused, set the saved value
          inputValue addFocusListener (new FocusListener() {
            def focusGained(event: FocusEvent) {
              if (message != None) {
                inputValue setText message.get.toString
              }
            }

            // Save the value the user has typed when the focus is lost
            def focusLost(event: FocusEvent) {
              if (inputValue.getText != "")
                parameters += inputHandler objectFromString (input, methodName, inputValue getText)
            }
          })

          inputValue addListener (SWT.KeyDown, new Listener() {
            def handleEvent(event: Event) = {
              // Save the value the user has typed when the user presses the enter key, update the display and run the code 
              if (event.keyCode == 13) {
                parameters += inputHandler objectFromString (input, methodName, inputValue getText)
                update
                compile
              }
            }
          })
        } else
          inputValue setText "unsupported type."

        // Add to the y value, so that the possible next input box will be below the previous 
        y += point.y
      }
      
      // Add vars from parsing AST 
      compiler.startParsing(unit, methodName)
      var i = 0
      for (v <- ar) {
        val name = v.fragments.get(0).asInstanceOf[VariableDeclarationFragment].getName
        val result = MirrorASTHelper.readFile(name.toString)
        val inputLabel = new Label(group, SWT.NONE)
        inputLabel setLocation (0, y)
        inputLabel setText v.getType + " " + name.toString + " = "  + result.replace("\n", "") + " | "
        inputLabel pack
        
        y += inputLabel.getSize.y
        i += 1
      }
    }

    // Add label for printing error message
    errorLabel = new Label(group, SWT.NONE)
    errorLabel setText errorMessage
    errorLabel setLocation (0, group.getSize.y - 18)
    errorLabel pack

    // Add label for printing return value
    returnLabel = new Label(group, SWT.NONE)
    returnLabel setText returnMessage
    returnLabel setLocation (0, group.getSize.y - 18 - errorLabel.getSize.y)
    returnLabel pack

    // Required to return some type, because I'm calling this method recursively
    0
  }

  // Error message clearing and setting
  def clearErrorMessage() = errorMessage = ""
  def setErrorMessage(s: String) = {
    val index = s indexOf className + ".java"
    errorMessage = "Line " + (s substring (index + className.length + ".java".length))
  }

  // Return message clearing and setting
  def clearReturnMessage() = returnMessage = ""
  def setReturnMessage(s: String) = {
    returnMessage = "Return value is " + s
    returnLabel setText returnMessage
    returnLabel pack
  }

  // Get the current caret position in the source file
  //  def caretPosition = editor.getAdapter(classOf[Control]).asInstanceOf[StyledText].getCaretOffset

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
    for (itype: IType <- types) {
      val methods = itype.getMethods
      for (method <- methods) {
        if (caretPosition >= method.getSourceRange.getOffset && caretPosition <= method.getSourceRange.getLength + method.getSourceRange.getOffset) {
          name = method.getElementName
        }
      }
    }
    name
  }

  // Compile and run the current method
  def compile = {
    compiler.compile(document.get, (packageName + "." + className), methodName, parameters.toArray, unit)
  }

  def dispose(): Unit = {
    document.removeDocumentListener(this)
  }
}