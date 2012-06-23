package mirror

import java.util.regex.Pattern

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

class InputHandler {
  val savedInputs = new HashMap[String, String]

  def objectFromString(parameterName: String, methodName: String, string: String) = {
    //    val inputType = parameterName.split(" ")(0)
    val a = intArrayFromString(string)
    savedInputs.put(parameterName + methodName, string)
    a
  }

  def intArrayFromString(s: String) = {
    val p = Pattern.compile("\\d+")
    val m = p.matcher(s)
    val list = new ArrayBuffer[Int]
    while (m find) {
      list += m.group.toInt
    }
    list.toArray
  }
}