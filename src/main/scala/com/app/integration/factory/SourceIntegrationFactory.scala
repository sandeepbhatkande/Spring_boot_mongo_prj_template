package com.app.integration.factory

import com.app.integration.inbound.api.bean.{EASeSourceInboundIntegrationManagerBean, SESourceInboundIntegrationManagerBean, SKSourceInboundIntegrationManagerBean}
import com.app.integration.inbound.api.manager.SourceInboundIntegrationManager
import com.app.integration.inbound.constant.IntegrationConstant
import com.app.integration.outbound.api.bean.{EASeSourceOutboundIntegrationManagerBean, SESourceOutboundIntegrationManagerBean, SKSourceOutboundIntegrationManagerBean}
import com.app.integration.outbound.api.manager.SourceOutboundIntegrationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SourceIntegrationFactory {

  @Autowired
  var skInboundIntegrationManager: SKSourceInboundIntegrationManagerBean = _
  @Autowired
  var seInboundIntegrationManager: SESourceInboundIntegrationManagerBean = _
  @Autowired
  var easeInboundIntegrationManager: EASeSourceInboundIntegrationManagerBean = _
  @Autowired
  var skOutboundIntegrationManager: SKSourceOutboundIntegrationManagerBean = _
  @Autowired
  var seOutboundIntegrationManager: SESourceOutboundIntegrationManagerBean = _
  @Autowired
  var easeOutboundIntegrationManager: EASeSourceOutboundIntegrationManagerBean = _

    def getInboundSourceIntegrationManager(enterpriseType: String): SourceInboundIntegrationManager = {

      val inboundIntegrationSourceManager: SourceInboundIntegrationManager = null

      if(IntegrationConstant.SWIFTENTERPRISE.equals(enterpriseType)){
        seInboundIntegrationManager
      }else if(IntegrationConstant.SWIFTKANBAN.equals(enterpriseType)){
        skInboundIntegrationManager
      }else if(IntegrationConstant.SWIFTEASE.equals(enterpriseType)){
        easeInboundIntegrationManager
      }else{
        inboundIntegrationSourceManager
      }
    }

  def getOutboundSourceIntegrationManager(enterpriseType: String): SourceOutboundIntegrationManager = {

    val outboundIntegrationSourceManager: SourceOutboundIntegrationManager = null

    if(IntegrationConstant.SWIFTENTERPRISE.equals(enterpriseType)){
      seOutboundIntegrationManager
    }else if(IntegrationConstant.SWIFTKANBAN.equals(enterpriseType)){
      skOutboundIntegrationManager
    }else if(IntegrationConstant.SWIFTEASE.equals(enterpriseType)){
      easeOutboundIntegrationManager
    }else{
      outboundIntegrationSourceManager
    }
  }
}