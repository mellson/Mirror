package mirror

import scala.Array.canBuildFrom
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

class InputHandler {
  val savedInputs = new HashMap[String, String]

  def objectFromString(parameterName: String, methodName: String, userInput: String) = {
    savedInputs.put(parameterName + methodName, userInput)

    // Look for substrings
    val arraySplitterRegEx = """;""".r

    // Look for letters andgGet the basic type. int, double etc
    val lettersRegEx = """[A-Za-z]+""".r
    val baseType = lettersRegEx.findFirstIn(parameterName).get.toLowerCase

    // Look for nested types - have to escape the first [ even though it is in 3 quotes
    val nestingRegEx = """\[[]]""".r

    // How many arrays are nested
    val depth = nestingRegEx.findAllIn(parameterName).length

    // Split the userinput into an iterator
    var values = arraySplitterRegEx.split(userInput)
    if (values.length == 0)
      values = Array(userInput)

    if (depth <= 1)
      valueFromInput(values, depth, baseType).asInstanceOf[Object]
    else
      null
  }

  def valueFromInput(value: Array[String], depth: Int, typeString: String): Any = (depth, typeString) match {
    case (0, "byte") => value(0).trim.toByte
    case (1, "byte") => value.map(_.trim.toByte)
    case (0, "short") => value(0).trim.toShort
    case (1, "short") => value.map(_.trim.toShort)
    case (0, "int") => value(0).trim.toInt
    case (1, "int") => value.map(_.trim.toInt)
    case (0, "long") => value(0).trim.toLong
    case (1, "long") => value.map(_.trim.toLong)
    case (0, "float") => value(0).trim.toFloat
    case (1, "float") => value.map(_.trim.toFloat)
    case (0, "double") => value(0).trim.toDouble
    case (1, "double") => value.map(_.trim.toDouble)
    case (0, "char") => value(0).trim.toInt.toChar
    case (1, "char") => value.map(_.trim.toInt.toChar)
    case (0, "string") => value(0)
    case (1, "string") => value
    case (0, "boolean") => boolFromString(value(0).trim)
    // If it's a bool array, I can't use my helper method as it will get the wrong type
    case (1, "boolean") => value.map(_.trim.toBoolean)
  }

  def boolFromString(s: String) = s.toLowerCase match {
    case "true" => true
    case "false" => false
    case "yes" => true
    case "no" => false
    case "1" => true
    case "0" => false
    case _ => false
  }

  def allowedInput(parameterName: String) = {
    // Look for nested types - have to escape the first [ even though it is in 3 quotes
    val nestingRegEx = """\[[]]""".r

    // How many arrays are nested
    val depth = nestingRegEx.findAllIn(parameterName).length

    if (depth <= 1)
      true
    else
      false
  }

  def isInputArray(parameterName: String) = {
    // Look for nested types - have to escape the first [ even though it is in 3 quotes
    val nestingRegEx = """\[[]]""".r

    // How many arrays are nested
    val depth = nestingRegEx.findAllIn(parameterName).length

    if (depth == 1)
      true
    else
      false
  }
}