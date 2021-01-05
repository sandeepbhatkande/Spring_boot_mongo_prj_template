package com.app.framework.login.webservice.security.config

import java.util

import com.app.framework.login.webservice.security.helper.SecurityHelper
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import org.springframework.util.{CollectionUtils, MultiValueMap}
import org.springframework.web.servlet.handler.AbstractHandlerMapping
import org.springframework.web.util.UrlPathHelper

import scala.collection.JavaConverters._

@Component
class XssHandlerMappingPostProcessor extends BeanPostProcessor{

  @throws[BeansException]
  override def postProcessBeforeInitialization(a_bean: Object, a_beanName: String): Object = a_bean

  @throws[BeansException]
  override def postProcessAfterInitialization(a_bean: Object, a_beanName: String): Object = {
    a_bean match {
      case w_ahm: AbstractHandlerMapping =>
        val w_urlPathHelper: XssUrlPathHelper = new XssUrlPathHelper
        w_ahm.setUrlPathHelper(w_urlPathHelper)
      case _ =>
    }

    a_bean
  }

  class XssUrlPathHelper extends UrlPathHelper {

    override def decodePathVariables(a_request: HttpServletRequest, vars: util.Map[String, String]): util.Map[String, String] = {
      val w_result : util.Map[String, String]  = super.decodePathVariables(a_request, vars)
      if (!w_result.isEmpty) {
        for (w_key <- w_result.keySet().asScala) {
          w_result.put(w_key, cleanXSS(w_result.get(w_key)))
        }
      }

      w_result
    }

    override def decodeMatrixVariables(request: HttpServletRequest, vars: MultiValueMap[String, String]): MultiValueMap[String, String] = {
      val w_mvm = super.decodeMatrixVariables(request, vars)
      if (!CollectionUtils.isEmpty(w_mvm)) {
        for (w_key <- w_mvm.keySet.asScala) {
          val w_value = w_mvm.get(w_key)
          var i = 0
          while (i < w_value.size) {
            w_value.set(i, cleanXSS(w_value.get(i)))
            i += 1
          }
        }
      }

      w_mvm
    }

    private def cleanXSS(a_value: String) = {
      val w_securityHelper : SecurityHelper = new SecurityHelper(a_value)
      val w_cleanedData : String = w_securityHelper.validate()

      w_cleanedData
    }
  }
}
