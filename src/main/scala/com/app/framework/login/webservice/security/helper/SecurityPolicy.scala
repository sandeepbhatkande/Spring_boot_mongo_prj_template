package com.app.framework.login.webservice.security.helper

import java.io.InputStream

import com.app.framework.util.general.WebUtil
import org.owasp.validator.html.Policy

object SecurityPolicy {

  var m_policy : Policy = _

  def getPolicy: Policy = {
    var w_input : InputStream  = null
    try
    {
      if (m_policy == null) {
        w_input = Thread.currentThread().getContextClassLoader.getResourceAsStream("antisamy.xml")
        m_policy = Policy.getInstance(w_input)
      }
      m_policy
    }
    catch {
      case exp: Exception =>
        WebUtil.logExceptionTrace(exp, null)
        throw exp
    }
    finally {
      if (w_input != null)
        w_input.close()
    }
  }
}
