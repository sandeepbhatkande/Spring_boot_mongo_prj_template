package com.app.core.crud.app.service

import com.app.core.crud.app.dataobject.dao.AppConfigDAO
import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.otherOperations.onBoarding.bean.OnBoardingManagerBean
import com.app.framework.util.app.ApplicationProperties
import com.app.framework.version.service.VersionService
import com.app.integration.inbound.constant.IntegrationConstant
import com.app.integration.inbound.helper.InboundIntegrationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "AppService")
class AppService() {

  @Autowired
  var m_onBoardingManagerBean: OnBoardingManagerBean = _

  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  @Autowired
  var m_appProps: ApplicationProperties = _

  @Autowired
  var m_versionService: VersionService = _

  def getConfig(a_enterpriseId: String, a_sourcePrjId: Int, a_extPrjId: String, a_userId: String, a_extUserId:String, a_loginId: String): AppConfigDAO = {
    val w_syncData: Map[String, Any] = m_onBoardingManagerBean.onBoardingProcess(a_enterpriseId, a_sourcePrjId, a_extPrjId, a_userId, a_extUserId, a_loginId)

    val w_ProjectId = w_syncData("ProjectId").toString
    val w_userData: UserDAO = w_syncData("UserDAO").asInstanceOf[UserDAO]

    var w_helpUrl: String = null
    val w_sourceURL: String = w_syncData("sourceURL").toString.replace("/rest", "/digite")
    val w_enterpriseType: String = w_syncData("enterpriseType").toString

    if ( IntegrationConstant.SWIFTENTERPRISE.equals(w_enterpriseType) ) {
      w_helpUrl = w_sourceURL + "/help//Project_Management/External_Work_Requests.htm"
    } else if( IntegrationConstant.SWIFTKANBAN.equals(w_enterpriseType) ) {
      w_helpUrl = "https://www.digite.com/knowledge-base/swiftkanban/article/working-with-external-work-requests/"
    } else if ( IntegrationConstant.SWIFTEASE.equals(w_enterpriseType) ) {
      w_helpUrl = "https://www.digite.com/knowledge-base/swiftease/article/working-with-external-work-requests/"
    }

    val w_appConfig = new AppConfigDAO {
      projectId = w_ProjectId
      user_data = w_userData
      user_fullname = w_userData.readFullName()
      enterpriseType = w_enterpriseType
      helpURL = w_helpUrl
      google_recaptcha_enabled = m_appProps.GOOGLE_RECAPTCHA_ENABLED
      google_recaptcha_sitekey = m_appProps.GOOGLE_RECAPTCHA_SITEKEY
      versionInfo = m_versionService.getCurrentVersion
    }

    w_appConfig
  }
}