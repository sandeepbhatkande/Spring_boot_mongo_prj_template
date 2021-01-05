package com.app.framework.util.general

import com.app.framework.common.constant.GlobalConstant

object StringUtil {
	
	val m_ignoreDecimalList = List("duemonth")
	
	def isValidString(a_str: String*): Boolean = {
		var w_isValid = true

		a_str.foreach(w_str => {
			if (w_str == null || w_str.isEmpty)
				w_isValid = false
		})

		w_isValid
	}

	//Date Format param to be removed from DateUtil.getDateFromString
	def getFormattedString(a_dateFieldsList: List[Any], a_attributeName: String, a_attributeValue: String): String = {
		
		var w_value = a_attributeValue
		
		if ( m_ignoreDecimalList.contains(a_attributeName) ) {
			return a_attributeValue
		}
		
		if (a_dateFieldsList.contains(a_attributeName.toLowerCase)) {
			try {
				val w_attribDateValue = DateUtil.getDateFromString(a_attributeValue, "dd-MM-yyyy")
				
				w_value = DateUtil.getUIStringFromDate(w_attribDateValue)
			}
			catch {
				case _: Exception =>
					w_value = a_attributeValue
			}
		}
		
		if (a_attributeValue.matches("[-+]?\\d*\\.?\\d+")) {
			w_value = getNDecimalRoundOff(a_attributeValue.toDouble, 2).toString
		}
		
		w_value
	}
	
	def getNDecimalRoundOff(a_value: Double, a_round: Int): Double = {
		
		BigDecimal(a_value).setScale(a_round, BigDecimal.RoundingMode.HALF_UP).toDouble
	}

	def getWbsCode( a_prntWbs: String, a_index: Int): String ={
		val str_builder = new StringBuilder()

		if ( a_prntWbs != null && a_prntWbs.length() > 0 ) {
			str_builder.append(a_prntWbs).append(".")
		}

		str_builder.append(padString(String.valueOf(a_index), GlobalConstant.DEFAULT_QUEUE_WBS_PAD, "0", a_rightPad = false))
		str_builder.toString()
	}

	def padString(a_str: String, a_maxSize: Int, a_padChar: String, a_rightPad: Boolean): String ={
		val str_builder = new StringBuilder(a_maxSize)
		val w_str: String = if (a_str == null) "" else a_str
		val w_len: Int = a_str.length()

		if (a_rightPad)
			str_builder.append(w_str)

		for (_ <- w_len until a_maxSize) {
			str_builder.append(a_padChar)
		}

		if (!a_rightPad)
			str_builder.append(w_str)

		str_builder.toString()
	}

	/**
		* Replaces all occurances String with a specified string.
		* <br>
		* <br>
		* <b>Purpose:</b>
		* <br>
		* This method replaces a String with a specified String.
		* <br>
		* <br>
		*
		* <b>Algorithm:</b>
		* <br>
		* Loop across the length of the original String and get all the characters.
		* If these characters match with the String that has to be replaced and if the substring of the original string matches with the string to be replaced then add these characters to th StringBuffer .
		* Returns the StringBuffer .
		*
		* <br>
		* <br>
		*
		* @param a_original    The original String
		* @param a_target      The string to be replaced
		* @param a_replacement The substitute for a_Src
		*
		*                      <br>
		*                      <br>
		* @return String The string after replacement.
		*/
	def replaceString(a_original: String, a_target: String, a_replacement: String): String = { // This implements same functionality as string.replaceAll except the target is not a regex.
		if (a_original == null) return ""

		if (a_target == null || a_replacement == null) return a_original

		val w_builder = new StringBuilder(a_original.length + 100)
		w_builder.append(a_original)
		replaceSubstring(w_builder, a_target, a_replacement).toString
	}

	/**
		* Replaces all occurences of a string
		*
		* @param a_builder: StringBuilder
		* @param a_target: String
		* @param a_replacement: String
		* @return modified StringBuilder
		*/
	def replaceSubstring(a_builder: StringBuilder, a_target: String, a_replacement: String): StringBuilder = {
		if (a_builder == null) return new StringBuilder

		if (a_target == null || a_replacement == null) return a_builder

		val tlen = a_target.length
		val rlen = a_replacement.length
		var start = a_builder.indexOf(a_target)

		while ( {
			start >= 0
		}) {
			a_builder.replace(start, start + tlen, a_replacement)
			start = a_builder.indexOf(a_target, start + rlen)
		}

		a_builder
	}
}