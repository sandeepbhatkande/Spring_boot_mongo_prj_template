package com.app.framework.login.webservice.security.config

import java.io.IOException
import java.lang.reflect.Type

import com.app.framework.common.constant.GlobalConstant
import com.app.framework.login.webservice.security.helper.SecurityHelper
import com.app.framework.util.general.JSONUtil
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.http.{HttpInputMessage, MediaType}
import org.springframework.stereotype.Component

@Component
class XSSRequestBodyConverter(a_objectMapper: ObjectMapper, a_MediaType : MediaType) extends AbstractJackson2HttpMessageConverter(a_objectMapper, a_MediaType : MediaType) {

  def this(a_objectMapper: ObjectMapper) = this(a_objectMapper, new MediaType("application", "json"))

  def this() = this(JSONUtil.m_objMapper, new MediaType("application", "json"))

  def clean(a_content: String): String = {
    val w_securityHelper : SecurityHelper = new SecurityHelper(a_content)
    val w_cleanedData : String = w_securityHelper.validate()

    w_cleanedData
  }

  @throws[IOException]
  @throws[HttpMessageNotReadableException]
  override def read(a_type:Type, a_contextClass: Class[_] , a_inputMessage : HttpInputMessage) : Object = {
    val w_requestBody: Object  = super.read(a_type, a_contextClass, a_inputMessage)
    val w_class : Class[_] = a_type.asInstanceOf[Class[_]]
    val w_requestData = JSONUtil.m_objMapper.writeValueAsString(w_requestBody)
    var w_isRTF : Boolean = false

    w_requestBody match {
      case w_jsonNode: JsonNode =>
        if (w_jsonNode.has(GlobalConstant.HASRTFCONTENT) && w_jsonNode.get(GlobalConstant.HASRTFCONTENT) != null)
          w_isRTF = GlobalConstant.YES.equalsIgnoreCase(w_jsonNode.get(GlobalConstant.HASRTFCONTENT).asText)
      case _ =>
    }

    var w_value : String = null

    if (w_isRTF)
      w_value =  w_requestData
    else
      w_value = clean(w_requestData)

    val w_cleanedValue = JSONUtil.m_objMapper.readValue(w_value, w_class)
    w_cleanedValue.asInstanceOf[Object]
  }
}