package com.app.integration.outbound.api.bean

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.service.EnterpriseService
import com.app.framework.exception.AppAppException
import com.app.framework.util.app.QueryHelper
import com.app.framework.util.general.WebUtil
import com.app.integration.factory.SourceIntegrationFactory
import com.app.integration.inbound.helper.InboundIntegrationHelper
import com.app.integration.outbound.api.manager.SourceOutboundIntegrationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable.ArrayBuffer

@Component
class OutboundIntegrationManagerBean() {
  @Autowired
  var m_enterpriseService: EnterpriseService = _
  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _
  @Autowired
  var m_queryHelper: QueryHelper = _
  @Autowired
  var m_sourceIntegrationFactory: SourceIntegrationFactory = _

  var m_outboundIntegrationSourceManager: SourceOutboundIntegrationManager = _

  def synchronizeCardDataInSource(a_card: JsonNode, a_enterpriseID: String, a_projectCode: String, a_itemType: String, a_loginId: String, a_projectId:String) : JsonNode = {
    var m_errorList: ArrayBuffer[String] = new ArrayBuffer()

    try {
      val w_enterpriseDAO: EnterpriseDAO = m_enterpriseService.read(a_enterpriseID)

      m_outboundIntegrationSourceManager = m_sourceIntegrationFactory.getOutboundSourceIntegrationManager(w_enterpriseDAO.enterprisetype)
      m_outboundIntegrationSourceManager.synchronizeCardDataInSource(a_card, w_enterpriseDAO, a_projectCode, a_itemType, a_loginId, a_projectId)

    } catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        m_errorList += e.getMessage

        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }

  def sendMail (a_enterpriseID: String, a_to:String, a_subject:String, a_body:String): Unit = {
    var m_errorList: ArrayBuffer[String] = new ArrayBuffer()

    try {
      val w_enterpriseDAO: EnterpriseDAO = m_enterpriseService.read(a_enterpriseID)

      m_outboundIntegrationSourceManager = m_sourceIntegrationFactory.getOutboundSourceIntegrationManager(w_enterpriseDAO.enterprisetype)
      m_outboundIntegrationSourceManager.sendMail(w_enterpriseDAO, a_to, a_subject, a_body, null)
    } catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        m_errorList += e.getMessage

        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }
}