package mirror

import scala.Array.canBuildFrom
import scala.collection.mutable.HashMap

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
    val arrays = tempArrays.map(nestingRegEx.findAllIn(_)) //-- mŒske ikke helt korrekt her. skal separere arrays ud af nesting
      
    // How many arrays are nested
    val depth = nestingRegEx.findAllIn(parameterName).length

    // Split the userinput into an iterator
    val values = arraySplitterRegEx.findAllIn(userInput).toArray
    
    arrayFromInt(values, depth, baseType).asInstanceOf[Object]
  }

  def arrayFromInt(value: Array[String], depth: Int, typeString: String) : Any = (depth,typeString) match {
    case (0,"int") => value(0).toInt
    case (1,"int") => value.map(_.toInt)
    case (_,"int") => Array(arrayFromInt(value, depth-1, typeString))
    case (1,"double") => value.map(_.toDouble)
  }
}