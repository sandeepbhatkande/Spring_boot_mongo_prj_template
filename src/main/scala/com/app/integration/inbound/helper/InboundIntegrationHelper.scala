package com.app.integration.inbound.helper

import java.io.{BufferedReader, DataOutputStream, InputStreamReader}
import java.net.{HttpURLConnection, URL, URLConnection}
import java.security.cert.X509Certificate

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.framework.login.webservice.security.helper.SecurityHelper
import com.app.framework.util.app.ApplicationProperties
import com.app.framework.util.general.{JSONUtil, URLUtil, WebUtil}
import com.app.integration.inbound.constant.IntegrationConstant
import javax.net.ssl._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable

@Component
class InboundIntegrationHelper() {

  @Autowired
  var m_URLUtil: URLUtil = _

  @Autowired
  var m_appProps: ApplicationProperties = _

  def getRestAPIResponse (a_RESTUrl : String, a_contentType : String, a_methodType : String , a_data : String,
    a_tokenName: String, a_tokenValue : String) : String = {
    var w_urlCon : URLConnection = null
    var w_out : DataOutputStream = null
    var w_responseReader : InputStreamReader = null

    var w_statusCode : Int = 0
    var w_urlConnection : HttpURLConnection = null
    var w_isr : InputStreamReader = null
    var w_br : BufferedReader = null

    try {
      val w_url = new URL(a_RESTUrl)
      val w_isHTTPS = a_RESTUrl.toLowerCase.indexOf("https:") != -1
      val w_keepDataWithoutCleaning = a_RESTUrl.indexOf(IntegrationConstant.SWIFTKANBAN_REST_IFORM_DETAILS_URL) != -1 ||
        a_RESTUrl.indexOf(IntegrationConstant.SWIFTENTERPRISE_REST_EFORM_CREATE_DETAILS_URL) != -1 ||
        a_RESTUrl.indexOf(IntegrationConstant.SWIFTENTERPRISE_SEND_MAIL_URL) != -1 ||
        a_RESTUrl.indexOf(IntegrationConstant.SWIFTKANBAN_SEND_MAIL_URL) != -1

      if (w_isHTTPS) {
        // SSL Context initialization and configuration
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, Array(TrustAll), new java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier(VerifiesAllHostNames)

        m_URLUtil.setSystemProxy()

        w_urlCon = w_url.openConnection.asInstanceOf[HttpsURLConnection]

        if (a_tokenName != null && !"".equals(a_tokenName.trim))
          w_urlCon.setRequestProperty(a_tokenName, a_tokenValue)

        w_urlCon.setRequestProperty(IntegrationConstant.HEADER_SOURCE_KEY, IntegrationConstant.APPLICATION_LABEL)

        if (a_contentType != null)
          w_urlCon.setRequestProperty("Content-Type", a_contentType)

        w_urlCon.asInstanceOf[HttpsURLConnection].setRequestMethod(a_methodType)

        if (IntegrationConstant.METHOD_POST.equalsIgnoreCase(a_methodType)|| IntegrationConstant.METHOD_PUT.equalsIgnoreCase(a_methodType))
          w_urlCon.setDoOutput(true)

        w_urlCon.connect()

        if (a_data != null && (IntegrationConstant.METHOD_POST.equalsIgnoreCase(a_methodType) || IntegrationConstant.METHOD_PUT.equalsIgnoreCase(a_methodType))) {
          w_out = new DataOutputStream(w_urlCon.getOutputStream)
          var w_cleanedData : String = a_data
          if (!w_keepDataWithoutCleaning) {
            val w_securityHelper: SecurityHelper = new SecurityHelper(w_cleanedData)
            w_cleanedData = w_securityHelper.validate()
          }
          w_out.write(w_cleanedData.getBytes)
        }

        w_statusCode = w_urlCon.asInstanceOf[HttpsURLConnection].getResponseCode
      }
      else {

        m_URLUtil.setSystemProxy()

        w_urlCon = w_url.openConnection.asInstanceOf[HttpURLConnection]

        if (a_tokenName != null && !"".equals(a_tokenName.trim))
          w_urlCon.setRequestProperty(a_tokenName, a_tokenValue)

        if (a_contentType != null)
          w_urlCon.setRequestProperty("Content-Type", a_contentType)

        w_urlCon.asInstanceOf[HttpURLConnection].setRequestMethod(a_methodType)

        if (IntegrationConstant.METHOD_POST.equalsIgnoreCase(a_methodType) || IntegrationConstant.METHOD_PUT.equalsIgnoreCase(a_methodType))
          w_urlCon.setDoOutput(true)

        w_urlCon.connect()

        if (a_data != null && (IntegrationConstant.METHOD_POST.equalsIgnoreCase(a_methodType) || IntegrationConstant.METHOD_PUT.equalsIgnoreCase(a_methodType))) {
          w_out = new DataOutputStream(w_urlCon.getOutputStream)
          var w_cleanedData : String = a_data
          if (!w_keepDataWithoutCleaning) {
            val w_securityHelper: SecurityHelper = new SecurityHelper(w_cleanedData)
            w_cleanedData = w_securityHelper.validate()
          }
          w_out.write(w_cleanedData.getBytes)
        }

        w_statusCode = w_urlCon.asInstanceOf[HttpURLConnection].getResponseCode
      }

      if (w_statusCode != 200 && w_statusCode != 201) {
        if (w_isHTTPS) {
          w_urlConnection = w_urlCon.asInstanceOf[HttpsURLConnection]
          w_isr = new InputStreamReader(w_urlConnection.getErrorStream)
          w_br = new BufferedReader(w_isr)
        }
        else {
          w_urlConnection = w_urlCon.asInstanceOf[HttpURLConnection]
          w_isr = new InputStreamReader(w_urlConnection.getErrorStream)
          w_br = new BufferedReader(w_isr)
        }
        val w_completeError = new StringBuilder
        var w_errorLine : String = w_br.readLine
        while (w_errorLine != null) {
          w_completeError ++= w_errorLine
          w_errorLine = w_br.readLine
        }
        w_br.close()
        throw new Exception("Failed to send rest request error:" + w_completeError.toString())
      }
      w_responseReader = new InputStreamReader(w_urlCon.getInputStream)
      w_br = new BufferedReader(w_responseReader)
      val w_response = new StringBuilder
      var w_responseLine: String = w_br.readLine
      while (w_responseLine != null) {
        w_response ++= w_responseLine
        w_responseLine = w_br.readLine
      }
      w_br.close()
      w_response.toString
    }
    catch
      {
        case exp : Exception =>
          val w_errorInfoMap: mutable.HashMap[Object, Object] = mutable.HashMap.empty[Object, Object]
          w_errorInfoMap += ("a_RESTUrl" -> a_RESTUrl)
          w_errorInfoMap += ("a_contentType" -> a_contentType)
          w_errorInfoMap += ("a_methodType" -> a_methodType)
          w_errorInfoMap += ("a_data" -> a_data)
          w_errorInfoMap += ("a_tokenName" -> a_tokenName)
          WebUtil.logExceptionTrace(exp, w_errorInfoMap)
          throw exp
      }
    finally {
      if (w_urlCon != null)
        w_urlCon match {
          case connection: HttpsURLConnection => connection.disconnect()
          case _ => w_urlCon.asInstanceOf[HttpURLConnection].disconnect()
        }
      if (w_out != null) w_out.close()
      if (w_responseReader != null) w_responseReader.close()
      if (w_isr != null) w_isr.close()
      if (w_br != null) w_br.close()
    }
  }

  // Bypasses both client and server validation.
  object TrustAll extends X509TrustManager {
    val getAcceptedIssuers: Null = null

    def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {
      //check client trust certificate
    }

    def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {
      //check server trust certificate
    }
  }

  // Verifies all host names by simply returning true.
  object VerifiesAllHostNames extends HostnameVerifier {
    def verify(s: String, sslSession: SSLSession) = true
  }

  def getRestAPIResponseForSwiftKanban(a_RESTUrl : String, a_contentType : String, a_methodType : String, a_data : String,
   a_enterpriseDAO : EnterpriseDAO, a_token : String) : String = {
    var w_token : String = null

    if (a_token != null)
      w_token = a_token
    else
      w_token = getTokenDetailsForSwiftKanban(a_enterpriseDAO)

    getRestAPIResponse(a_RESTUrl, a_contentType, a_methodType, a_data, IntegrationConstant.SWIFTKANBAN_REST_AUTHORIZATION_KEY, w_token)
  }

  def getRestAPIResponseForSwiftEnterprise(a_RESTUrl : String, a_contentType : String, a_methodType : String ,
   a_data : String, a_enterpriseDAO : EnterpriseDAO, a_token : String) : String = {
    var w_token : String = null

    if (a_token != null)
      w_token = a_token
    else
      w_token = getTokenDetailsForSwiftEnterprise(a_enterpriseDAO)

    getRestAPIResponse(a_RESTUrl, a_contentType, a_methodType, a_data, IntegrationConstant.SWIFTENTERPRISE_REST_AUTHORIZATION_KEY, w_token)
  }

  def getTokenDetails(a_enterpriseDAO : EnterpriseDAO) : String = {
    if (IntegrationConstant.SWIFTENTERPRISE.equals(a_enterpriseDAO.getEnterprisetype))
      getTokenDetailsForSwiftEnterprise(a_enterpriseDAO)
    else
      getTokenDetailsForSwiftKanban(a_enterpriseDAO)
  }

  private  def getTokenDetailsForSwiftEnterprise(a_enterpriseDAO : EnterpriseDAO) : String = {
    val w_TokenRESTAPIURL: String = getRESTBaseUrl(a_enterpriseDAO.source_url) + IntegrationConstant.SWIFTENTERPRISE_REST_TOKEN_URL
    val w_TokenType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_FORMURLENCODED
    val w_TokenMethodType: String = IntegrationConstant.METHOD_POST
    val w_sourceAdminLoginID: String = WebUtil.substituteEscapeCharacters(a_enterpriseDAO.getSource_loginid)
    val w_sourceAdminPassword: String  = WebUtil.substituteEscapeCharacters(a_enterpriseDAO.getSource_password)
    val w_tokenAuthenticationData: String = IntegrationConstant.SWIFTENTERPRISE_REST_TOKEN_LOGINID + "=" + w_sourceAdminLoginID + "&" +
      IntegrationConstant.SWIFTENTERPRISE_REST_TOKEN_PASSWORD + "=" + w_sourceAdminPassword

    val w_JSONResponse = getRestAPIResponse(w_TokenRESTAPIURL, w_TokenType, w_TokenMethodType, w_tokenAuthenticationData, null, null)
    val w_TokenJSON: JsonNode =  JSONUtil.mapStringToJSON(w_JSONResponse)

    val w_token: String = JSONUtil.getValueFromJSONNode(IntegrationConstant.SWIFTENTERPRISE_REST_TOKEN_DATA, w_TokenJSON)

    w_token
  }

  private def getTokenDetailsForSwiftKanban(a_enterpriseDAO : EnterpriseDAO) : String = {
    val w_TokenRESTAPIURL: String = getRESTBaseUrl(a_enterpriseDAO.source_url) + IntegrationConstant.SWIFTKANBAN_REST_TOKEN_URL
    val w_TokenType: String = "text/plain"
    val w_TokenMethodType: String = IntegrationConstant.METHOD_POST
    val w_sourceAdminLoginID: String = WebUtil.decryptWithB64(a_enterpriseDAO.getSource_loginid)
    val w_sourceAdminPassword: String  = WebUtil.decryptWithB64(a_enterpriseDAO.getSource_password)
    val w_encodedCredential = WebUtil.encryptWithB64(w_sourceAdminLoginID+":"+w_sourceAdminPassword)
    val w_encodedData =   IntegrationConstant.SWIFTKANBAN_REST_AUTHENTICATION_KEYWORD+" "+ w_encodedCredential  // Base64 encoding is required in Kanban for logind:password
    val w_tokenData: Map[String, String] = Map(IntegrationConstant.SWIFTKANBAN_REST_AUTHENTICATION_TOKEN -> w_encodedData)
    val w_tokenAuthenticationData : String = JSONUtil.convertMapToJson(w_tokenData).toString
    val w_JSONResponse = getRestAPIResponse(w_TokenRESTAPIURL, w_TokenType, w_TokenMethodType, w_tokenAuthenticationData,
      null, null)
    val w_TokenJSON: JsonNode =  JSONUtil.mapStringToJSON(w_JSONResponse)
    val str = w_TokenJSON.get("Response").get("details").get("authDetails")
    val w_token: String = JSONUtil.getValueFromJSONNode(IntegrationConstant.SWIFTKANBAN_REST_AUTHORIZATION_KEY, str)

    w_token
  }

  def getRESTBaseUrl( a_url: String ): String = {
    var w_url = a_url

    if ( m_appProps.VIRTUALIZATION_MOUNTEBANK_URL != null && !"".equals(m_appProps.VIRTUALIZATION_MOUNTEBANK_URL.trim) ) {
      w_url = m_appProps.VIRTUALIZATION_MOUNTEBANK_URL
    }

    w_url
  }
}