package com.app.framework.login.webservice.security.helper

import com.app.framework.util.app.MessageLogger
import com.app.framework.util.general.StringUtil
import org.owasp.validator.html.{AntiSamy, CleanResults, ScanException}

class SecurityHelper(a_input: String) {

  private val LOGGER = new MessageLogger(this.getClass.getName)

  def validate() : String =
  {
      var w_input: String = a_input
      try
      {
        if (SecurityPolicy.getPolicy != null)
        {
          val w_anti: AntiSamy = new AntiSamy()
          var w_cr: CleanResults = null
          if (w_input != null)
          {
            try
            {
              w_input = replaceSpecialChars(w_input)
              w_cr = w_anti.scan(w_input, SecurityPolicy.getPolicy())
              if (w_cr != null)
              {
                if (w_cr.getErrorMessages != null && w_cr.getErrorMessages.size > 0) LOGGER.fatal("XSS Attack : " + w_cr.getErrorMessages.toString)
                val w_cleanHtml = w_cr.getCleanHTML
                if (w_cleanHtml != null) w_input = reverseGetHTMLDisplayString(w_cleanHtml)
                if (w_input != null) w_input = reverseSpecialChars(w_input)
              }
            }
            catch
            {
              case e: ScanException =>
                LOGGER.fatal("Scan Exception ->" + e.getMessage)
              case e: Exception =>
                LOGGER.fatal("PolicyException Exception ->" + e.getMessage)
            }
          }
        }
        else
          LOGGER.info("No Policy File")
      }
      catch
      {
        case e1: Exception =>
          LOGGER.fatal("Preference Exception for antisamy->" + e1.getMessage)
      }
      if (a_input != null && !a_input.equals(w_input))
        LOGGER.fatal("original i/p:" + a_input+"&final i/p:"+w_input)
      w_input
  }

  private def reverseGetHTMLDisplayString(a_Str: String): String = {
    if (a_Str == null) return ""
    val w_builder = new StringBuilder(a_Str)
    StringUtil.replaceSubstring(w_builder, "&amp;", "&")
    StringUtil.replaceSubstring(w_builder, "&quot;", "\"")
    StringUtil.replaceSubstring(w_builder, "&lt;", "<")
    StringUtil.replaceSubstring(w_builder, "&gt;", ">")
    w_builder.toString
  }

  private def replaceSpecialChars(a_Str: String): String = {
    if (a_Str == null) return ""
    val w_builder = new StringBuilder(a_Str)
    StringUtil.replaceSubstring(w_builder, "&quot;", "SPECIALCHARDOUBLEQUOT")
    StringUtil.replaceSubstring(w_builder, "&#92;", "SPECIALCHARBACKSLASH")
    w_builder.toString
  }

  private def reverseSpecialChars(a_Str: String): String = {
    if (a_Str == null) return ""
    val w_builder = new StringBuilder(a_Str)
    StringUtil.replaceSubstring(w_builder, "SPECIALCHARDOUBLEQUOT", "&quot;")
    StringUtil.replaceSubstring(w_builder, "SPECIALCHARBACKSLASH", "&#92;")
    w_builder.toString
  }

}