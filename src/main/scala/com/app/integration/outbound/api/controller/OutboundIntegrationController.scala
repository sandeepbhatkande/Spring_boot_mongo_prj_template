package com.app.integration.outbound.api.controller

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.service.EnterpriseService
import com.app.integration.outbound.api.OutboundIntegrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController(value = "outboundintegrationcontroller")
@RequestMapping(value = Array("/integration/outbound"))
class OutboundIntegrationController(@Autowired m_service: OutboundIntegrationService, @Autowired m_enterpriseService: EnterpriseService)
{
  @PostMapping(path = Array("/synchronizeCardDataInSource/{enterpriseID}/{projectCode}/{itemType}/{loginId}/{projectId}"))
  def synchronizeCardDataInSource(@RequestBody a_card: JsonNode, @PathVariable("enterpriseID") a_enterpriseID: String, @PathVariable("projectCode") a_projectCode: String,
                     @PathVariable("itemType") a_itemType: String,   @PathVariable("loginId") a_loginId: String, @PathVariable("projectId") a_projectId: String) : JsonNode = {
    m_service.synchronizeCardDataInSource(a_card, a_enterpriseID, a_projectCode, a_itemType, a_loginId, a_projectId)
  }
}