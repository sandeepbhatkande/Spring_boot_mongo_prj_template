package com.app.integration.inbound.api.service

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.framework.util.general.{ResponseHelper, WebUtil}
import com.app.integration.inbound.api.bean.InboundIntegrationManagerBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.stereotype.Service

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

@Service(value = "InboundIntegrationService")
class InboundIntegrationService(@Autowired m_bean : InboundIntegrationManagerBean){

  def synchronizeUser(a_enterpriseId : String, a_sourceUserId : String, a_sourceExternalUserID : String, a_loginID : String, a_token : String): UserDAO = {
    m_bean.synchronizeUser(a_enterpriseId, a_sourceUserId, a_sourceExternalUserID, a_loginID, null, a_token)
  }

  def synchronizeFromSourceApp(a_entId: String, a_sourcePrjId: Int, a_extPrjId: String, a_userId: String, a_extUserId: String, a_loginId: String): ResponseEntity[Object] = {
    var a_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
    var a_respMsg: ArrayBuffer[String] = new ArrayBuffer[String]()
    var w_syncRespMap: Map[String, Any] = Map()

    try {
      val w_syncDataMap: Map[String, Any] = m_bean.synchronizeFromSourceApp(a_entId, a_sourcePrjId, a_extPrjId, a_userId, a_extUserId, a_loginId)

      w_syncRespMap += ("syncData" -> w_syncDataMap)
      a_respMsg += "All the entities synchronized successfully"

      ResponseHelper.getResponse("Success", a_respMsg, w_syncRespMap, HttpStatus.OK)
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        a_errorList += e.getMessage

        ResponseHelper.getResponse("Error", a_errorList, w_syncRespMap, HttpStatus.UNPROCESSABLE_ENTITY)
    }
  }
}