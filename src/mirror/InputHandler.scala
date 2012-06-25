package mirror

import scala.Array.canBuildFrom
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

class InputHandler {
  val savedInputs = new HashMap[String, String]

  def objectFromString(parameterName: String, methodName: String, userInput: String) = {
    savedInputs.put(parameterName + methodName, userInput)

    // Look for numbers
    val arraySplitterRegEx = """\d*\.?\d+""".r

    // Look for letters andgGet the basic type. int, double etc
    val lettersRegEx = """[A-Za-z]+""".r
    val baseType = lettersRegEx.findFirstIn(parameterName).get.toLowerCase
    
    // Look for nested types - have to escape the first [ even though it is in 3 quotes
    val nestingRegEx = """\[[]]""".r
    
    // Get each array separated if there is nesting
    val separateNestingRegEx = """(\{|\(|\[).*?(\}|\)|\])""".r
    val tempArrays = separateNestingRegEx.findAllIn(userInput)
    val nestedArrays = new ArrayBuffer[Array[String]]()
    for (array<-tempArrays) {
      nestedArrays += arraySplitterRegEx.findAllIn(array).toArray
    }
      
    // How many arrays are nested
    val depth = nestingRegEx.findAllIn(parameterName).length

    // Split the userinput into an iterator
    val values = arraySplitterRegEx.findAllIn(userInput).toArray
    
    if (depth<=1)
    	valueFromInput(values, depth, baseType).asInstanceOf[Object]
    else {
      val result = new ArrayBuffer[Any]
      for (array <- nestedArrays)
        result += array.map(_.toInt)
      result.toArray.asInstanceOf[Object]
    }
  }

  def valueFromInput(value: Array[String], depth: Int, typeString: String) : Any = (depth,typeString) match {
    case (0,"int") => value(0).toInt
    case (1,"int") => value.map(_.toInt)
    case (0,"double") => value(0).toDouble
    case (1,"double") => value.map(_.toDouble)
  }
}