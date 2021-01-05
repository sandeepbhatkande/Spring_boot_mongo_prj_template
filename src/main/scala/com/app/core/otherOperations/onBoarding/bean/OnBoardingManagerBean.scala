package com.app.core.otherOperations.onBoarding.bean

import com.app.integration.inbound.api.bean.InboundIntegrationManagerBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.immutable.Map

@Component
class OnBoardingManagerBean() {

  @Autowired
  var m_inboundBean: InboundIntegrationManagerBean = _

  def onBoardingProcess(entId: String, prjId: Int, extPrjId: String, userId: String, extUserId: String, loginId: String): Map[String, Any] = {
    // This api will synchronize Project, User and Card Types from Source App to app Starts
    m_inboundBean.synchronizeFromSourceApp(entId, prjId, extPrjId, userId, extUserId, loginId)
  }
}