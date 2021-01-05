package com.app.integration.inbound.api.bean

import java.util

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.service.EnterpriseService
import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.crud.user.helper.UserJsonHelper
import com.app.framework.exception.AppAppException
import com.app.framework.util.app.QueryHelper
import com.app.framework.util.general.{JSONUtil, WebUtil}
import com.app.integration.factory.SourceIntegrationFactory
import com.app.integration.inbound.api.manager.SourceInboundIntegrationManager
import com.app.integration.inbound.helper.InboundIntegrationHelper
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

@Component
class InboundIntegrationManagerBean() {

  @Autowired
  var m_enterpriseService: EnterpriseService = _
  @Autowired
  var m_userService: UserJsonHelper = _
  @Autowired
  var m_objectMapper: ObjectMapper = _
  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _
  @Autowired
  var m_queryHelper: QueryHelper = _
  @Autowired
  var m_sourceIntegrationFactory: SourceIntegrationFactory = _
  @Autowired
  var m_template : MongoTemplate = _

  var m_inboundIntegrationSourceManager: SourceInboundIntegrationManager = _

  /**
    * @param a_enterpriseId : EnterpriseId created while configuring app in source system.
    * @param a_sourceUserID : UserId in source system . e.g : 12345
    * @param a_sourceExternalUserID : For SwiftKanban a_sourceUserID and a_sourceExternalUserID is same . e.g : 12345
    * @param a_loginID : loginId of the user in source system. e.g : nilesh@gandiva.com
    * @param a_enterpriseDAO : Enterprise data object in app
    * @return : UserDAO object created in app on the basis of data fetched from Source system for logged in user.
    */
  def synchronizeUser(a_enterpriseId : String, a_sourceUserID : String, a_sourceExternalUserID : String,
   a_loginID : String, a_enterpriseDAO: EnterpriseDAO, a_token : String): UserDAO = {
    val w_enterpriseDAO : EnterpriseDAO = if (a_enterpriseDAO == null) m_enterpriseService.read(a_enterpriseId) else a_enterpriseDAO

    m_inboundIntegrationSourceManager = m_sourceIntegrationFactory.getInboundSourceIntegrationManager(w_enterpriseDAO.enterprisetype)
    val w_sourceUserDAO : UserDAO = m_inboundIntegrationSourceManager.getSourceUserDetails(w_enterpriseDAO, a_sourceUserID, a_sourceExternalUserID, a_loginID, a_token)

    val w_criteriaMap = Map("enterpriseid" -> a_enterpriseId, "source_userid" -> a_sourceUserID)
    val w_userList  : util.List[util.HashMap[String, Any]] = m_queryHelper.read(w_criteriaMap, "user")
    var w_userDAO : UserDAO = null
    var w_appUserID : String = null

    if (w_userList != null && !w_userList.isEmpty) {
      val w_userJSONNode = JSONUtil.convertListOfMapToListOfJsonNode(w_userList)

      w_userDAO = JSONUtil.mapJSONToObject(w_userJSONNode.head, classOf[UserDAO]).asInstanceOf[UserDAO]
      w_appUserID = w_userDAO.getId
      w_sourceUserDAO.setId(w_appUserID)
      w_sourceUserDAO.setCreationdate(w_userDAO.getCreationdate)
      m_userService.update(w_sourceUserDAO)
    } else {
      val userId = m_userService.create(w_sourceUserDAO)

      w_sourceUserDAO.id = userId
    }

    w_sourceUserDAO
  }

  /**
    *
    * @param a_entId : EnterpriseId in app , created while configuring IssueController in source system.
    * @param a_sourcePrjId : projectId in source system (In this case source system is SwiftKanban). e.g : 12345
    * @param a_extPrjCode : For SwiftKanban a_sourceProjectID and a_sourceExternalProjectCode is same . e.g : 12345
    * @param a_userId : UserId in source system . e.g : 12345
    * @param a_extUserId : For SwiftKanban a_sourceUserID and a_sourceExternalUserID is same . e.g : 12345
    * @param a_loginId : loginId of the user in source system. e.g : nilesh@gandiva.com
    * @return : Map with following keys : "ProjectId", "boardId", "firstLoad", "roadmapPlan", "UserDAO", "enterpriseType" and "sourceURL"
    */
  def synchronizeFromSourceApp(a_entId: String, a_sourcePrjId: Int, a_extPrjCode: String, a_userId: String, a_extUserId: String, a_loginId: String): Map[String, Any] = {
    var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
    var w_syncData: Map[String, Any] = Map()

    try {
      val w_enterpriseDAO : EnterpriseDAO = m_enterpriseService.read(a_entId)
      val w_token = m_inboundIntegrationHelper.getTokenDetails(w_enterpriseDAO)
      val w_userDAO: UserDAO = synchronizeUser(a_entId, a_userId, a_extUserId, a_loginId, w_enterpriseDAO, w_token)
      val w_synchCardProjectList: util.List[util.LinkedHashMap[String, Any]] = new util.ArrayList()

      val w_singleProjectData: util.LinkedHashMap[String, Any] = new util.LinkedHashMap[String, Any]()
      w_singleProjectData.put("sourceProjectId", a_sourcePrjId)
      w_singleProjectData.put("sourceExternalProjectCode", a_extPrjCode)

      w_synchCardProjectList.add(w_singleProjectData)

      w_syncData += ("UserDAO" -> w_userDAO, "enterpriseType" -> w_enterpriseDAO.enterprisetype, "sourceURL" -> w_enterpriseDAO.source_url)

      w_syncData
    } catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }
}