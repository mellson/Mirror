package mirror

import scala.collection.mutable.HashMap
import java.util.regex.Pattern
import scala.collection.mutable.ArrayBuffer

class InputHandler {
  val savedInputs = new HashMap[String,Object]
  
  def objectFromString(parameterName: String, methodName: String, string: String) : Object = {
    val inputType = parameterName.split(" ")(0)
    val numbers = Array(1,2,3,4,5)
    savedInputs.put(parameterName+methodName, string)
    return numbers
  }
//  def
//  
//  def handle = {
//    val p = Pattern.compile("-?\\d+")
//    val m = p.matcher(inputValue getText)
//    val list = new ArrayBuffer[Int]
//    while (m find) {
//    	list += m.group.toInt
//    	}
//  }
}