package com.app.framework.util.general

import scala.collection.mutable

object CollectionUtil {

  /**
    * This Method will convert HashMap to Key Value String Of the Form key1:value1 key2:value2
    *
    * @param a_paramMap: mutable.HashMap[Object, Object]
    * @return String
    */
  def convertHashMapToKeyValueString(a_paramMap: mutable.HashMap[Object, Object]): String = {
    if (a_paramMap != null) {
      val w_argList = new StringBuilder()
      a_paramMap foreach { case (key, value) => w_argList.append(if (key != null) key.toString else "").append(":").append(if (value != null) value.toString else "").append(" ") }

      return w_argList.toString
    }

    null
  }
}