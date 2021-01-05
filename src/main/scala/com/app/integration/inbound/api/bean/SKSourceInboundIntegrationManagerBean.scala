package com.app.integration.inbound.api.bean

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.framework.common.constant.GlobalConstant
import com.app.framework.exception.AppAppException
import com.app.framework.util.general.JSONUtil
import com.app.integration.inbound.api.manager.SourceInboundIntegrationManager
import com.app.integration.inbound.constant.IntegrationConstant
import com.app.integration.inbound.helper.InboundIntegrationHelper
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class SKSourceInboundIntegrationManagerBean extends SourceInboundIntegrationManager {

  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  /**
   * @author : nratta
   * @param a_enterpriseDAO : Enterprise data object in app
   * @param a_sourceUserID : UserId in source system (In this case source system is SwiftKanban) . e.g : 12345
   * @param a_sourceExternalUserID : For SwiftKanban a_sourceUserID and a_sourceExternalUserID is same . e.g : 12345
   * @return UserDAO object created in app on the basis of data fetched from Source system for logged in user.
   */
  override def getSourceUserDetails(a_enterpriseDAO: EnterpriseDAO, a_sourceUserID: String,
    a_sourceExternalUserID: String, a_loginID: String, a_token : String): UserDAO = {

    val w_UserDetailsRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
      IntegrationConstant.SWIFTKANBAN_REST_USER_DETAILS_URL + a_sourceExternalUserID

    val w_contentType: String = null
    val w_methodType: String = IntegrationConstant.METHOD_GET
    val w_UserResponse = m_inboundIntegrationHelper.getRestAPIResponseForSwiftKanban(w_UserDetailsRESTURL, w_contentType, w_methodType, null, a_enterpriseDAO, a_token)
    val w_UserJSON: JsonNode =  JSONUtil.mapStringToJSON(w_UserResponse)
    val w_user = w_UserJSON.at(IntegrationConstant.SWIFTKANBAN_REST_RESPONSE_KEY_USER)
    val w_userDAO = new UserDAO()

    if (w_user != null) {
      w_userDAO.email = if(w_user.hasNonNull("emailAddress")) w_user.get("emailAddress").textValue() else null
      w_userDAO.enterpriseid = a_enterpriseDAO.id
      w_userDAO.firstname = if(w_user.hasNonNull("firstName"))  w_user.get("firstName").textValue() else null
      w_userDAO.lastname = if(w_user.hasNonNull("lastName"))  w_user.get("lastName").textValue() else null
      w_userDAO.loginid = if(w_user.hasNonNull("loginId")) w_user.get("loginId").textValue()
      else throw new AppAppException(GlobalConstant.JSON_KEY_MISSING_NULL_VALUES + "loginId")
      w_userDAO.timezone =  if(w_user.hasNonNull("timeZone") && !"".equals(w_user.get("timeZone").textValue())) w_user.get("timeZone").textValue() else null
      w_userDAO.source_externaluserid = a_sourceExternalUserID
      w_userDAO.source_userid = a_sourceExternalUserID
      w_userDAO.status = GlobalConstant.STATUS_OPEN
    }
    else
      throw new AppAppException(GlobalConstant.JSON_KEY_MISSING_NULL_VALUES + IntegrationConstant.SWIFTKANBAN_REST_RESPONSE_KEY_USER)

    w_userDAO
  }
}