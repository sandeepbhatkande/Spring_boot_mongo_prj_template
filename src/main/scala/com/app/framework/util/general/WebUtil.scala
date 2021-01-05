package com.app.framework.util.general

import java.io.{PrintWriter, StringWriter}
import java.net.URLEncoder
import java.util.Base64

import com.app.framework.common.constant.GlobalConstant
import com.app.framework.util.app.MessageLogger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object WebUtil {

  private val LOGGER = new MessageLogger(this.getClass.getName)

  private var m_exceptionList = ArrayBuffer[Throwable]()

  /**
    * To Log the StackTrace and the Argumnts of the Method for which error has occured.
    *
    * @param a_e: Throwable
    * @param a_paramMap: mutable.HashMap[Object, Object]
    */

  def logExceptionTrace(a_e: Throwable, a_paramMap: mutable.HashMap[Object, Object]): Unit = {
    var w_keyValueStr : String = null
    if (a_paramMap!= null)
      w_keyValueStr = CollectionUtil.convertHashMapToKeyValueString(a_paramMap)
    val w_traceString = getStackTraceAsString(a_e.asInstanceOf[Exception])
    if (!m_exceptionList.contains(a_e)) {
      if (m_exceptionList.size == 50) m_exceptionList.remove(0)
      m_exceptionList += a_e
      LOGGER.fatal("Exception===> " + (if (w_keyValueStr != null) "Values ===>" + w_keyValueStr else "No Arguments") + w_traceString)
      try {
        var w_stackTrace = w_traceString
        w_stackTrace = if (w_stackTrace.length > 3800) w_stackTrace.substring(0, 3800)
        else w_stackTrace
      } catch {
        case a_ex: Exception =>
          LOGGER.fatal(getStackTraceAsString(a_ex))
      }
    }
  }

  def getStackTraceAsString(a_Exp: Exception): String = {
    val w_Str = new StringWriter
    a_Exp.printStackTrace(new PrintWriter(w_Str))
    w_Str.toString
  }

  def encryptString(a_str: String): String = {

    val bCryptPasswordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder(11)
    bCryptPasswordEncoder.encode(a_str)
  }

  def encryptWithB64(a_str: String): String = {
    val encodedStr = if(a_str != null) Base64.getEncoder.encodeToString(a_str.getBytes("utf-8")) else ""
    encodedStr
  }

  def decryptWithB64(a_str: String): String = {
    val decodedStr: String = if(a_str != null) new String(Base64.getDecoder.decode(a_str)) else ""
    decodedStr
  }

  @throws[Exception]
  def urlEncode(a_url: String): String = {
    if (a_url != null)
      return URLEncoder.encode(a_url, GlobalConstant.UTF8_CHARACTER_ENCODING)
    a_url
  }

  @throws[Exception]
  def substituteEscapeCharacters(a_url: String): String = {
    if (a_url != null)
      return URLEncoder.encode(a_url, "UTF-8").replace("+", "%20")
    a_url
  }

  def removeCRLF(a_url: String): String = {
    var w_url = StringUtil.replaceString(a_url, "\r\n","")
    w_url = StringUtil.replaceString(w_url, "%0d%0a","")

    w_url
  }
}