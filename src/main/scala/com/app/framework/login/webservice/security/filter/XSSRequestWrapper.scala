package com.app.framework.login.webservice.security.filter

import com.app.framework.login.webservice.security.helper.SecurityHelper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class XSSRequestWrapper(servletRequest: HttpServletRequest) extends HttpServletRequestWrapper(servletRequest: HttpServletRequest) {

  override def getParameterValues(parameter: String): Array[String] = {
    val w_values = super.getParameterValues(parameter)
    if (w_values == null) return null
    val w_count = w_values.length
    val w_cleanedValues = new Array[String](w_count)
    for (i <- 0 until w_count) {
      w_cleanedValues(i) = validate(w_values(i))
    }
    w_cleanedValues
  }

  override def getParameter(parameter: String): String = {
    val value = super.getParameter(parameter)
    validate(value)
  }

  override def getHeader(name: String): String = {
    val value = super.getHeader(name)
    validate(value)
  }

  private def validate(value: String) : String = {
    val w_securityHelper: SecurityHelper = new SecurityHelper(value)
    w_securityHelper.validate()
  }
}