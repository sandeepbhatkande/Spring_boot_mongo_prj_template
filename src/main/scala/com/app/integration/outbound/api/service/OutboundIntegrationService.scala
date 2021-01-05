package com.app.integration.outbound.api.service

import com.app.integration.outbound.api.bean.OutboundIntegrationManagerBean
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "OutboundIntegrationService")
class OutboundIntegrationService(@Autowired m_bean : OutboundIntegrationManagerBean) {

  def synchronizeCardDataInSource(a_card: JsonNode, a_enterpriseID: String, a_projectCode: String, a_itemType: String, a_loginId: String, a_projectId:String) : JsonNode = {
    m_bean.synchronizeCardDataInSource(a_card, a_enterpriseID, a_projectCode, a_itemType, a_loginId, a_projectId)
  }

  def sendMail(a_enterpriseID: String, a_to:String, a_subject:String, a_body:String): Unit = {
    m_bean.sendMail(a_enterpriseID, a_to, a_subject, a_body)
  }
}