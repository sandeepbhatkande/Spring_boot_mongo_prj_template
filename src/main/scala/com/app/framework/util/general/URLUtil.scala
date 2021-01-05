package com.app.framework.util.general

import com.app.framework.util.app.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class  URLUtil(@Autowired m_appProps: ApplicationProperties) {

  val w_externalHTTPProxyIP:String = m_appProps.EXTERNAL_HTTP_PROXY_IP

  val w_externalHTTPProxyPort:String = m_appProps.EXTERNAL_HTTP_PROXY_PORT

  val w_externalHTTPNonProxyHosts:String = m_appProps.EXTERNAL_HTTP_NON_PROXY_HOSTS

  def setSystemProxy() : Unit = {

    if (w_externalHTTPProxyIP != null && !"".equals(w_externalHTTPProxyIP.trim)) {
      System.setProperty("http.proxyHost", w_externalHTTPProxyIP)
      System.setProperty("https.proxyHost", w_externalHTTPProxyIP)
    }

    if (w_externalHTTPProxyPort != null && !"".equals(w_externalHTTPProxyPort.trim)) {
      System.setProperty("http.proxyPort", w_externalHTTPProxyPort)
      System.setProperty("https.proxyPort", w_externalHTTPProxyPort)
    }

    if (w_externalHTTPNonProxyHosts != null && !"".equals(w_externalHTTPNonProxyHosts.trim)) {
      System.setProperty("http.nonProxyHosts", w_externalHTTPNonProxyHosts)
    }
  }

  def clearSystemProxy() : Unit = {

    if (w_externalHTTPProxyIP != null && !"".equals(w_externalHTTPProxyIP.trim)) {
      System.clearProperty("http.proxyHost")
      System.clearProperty("https.proxyHost")
    }

    if (w_externalHTTPProxyPort != null && !"".equals(w_externalHTTPProxyPort.trim)) {
      System.clearProperty("http.proxyPort")
      System.clearProperty("https.proxyPort")
    }

    if (w_externalHTTPNonProxyHosts != null && !"".equals(w_externalHTTPNonProxyHosts.trim)) {
      System.clearProperty("http.nonProxyHosts")
    }
  }
}