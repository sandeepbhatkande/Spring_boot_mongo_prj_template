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
import org.springframework.stereotype.Component

@Component
class SESourceInboundIntegrationManagerBean extends SourceInboundIntegrationManager {

  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  override def getSourceUserDetails(a_enterpriseDAO: EnterpriseDAO, a_sourceUserID: String,
    a_sourceExternalUserID: String, a_loginID: String, a_token : String): UserDAO = {

    val w_UserDetailsRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
      IntegrationConstant.SWIFTENTERPRISE_REST_USER_DETAILS_URL + a_sourceExternalUserID

    val w_contentType: String = null
    val w_methodType: String = IntegrationConstant.METHOD_GET
    val w_data = null

    val w_UserResponse = m_inboundIntegrationHelper.getRestAPIResponseForSwiftEnterprise(w_UserDetailsRESTURL, w_contentType,
      w_methodType, w_data, a_enterpriseDAO, a_token)
    val w_UserJSON: JsonNode =  JSONUtil.mapStringToJSON(w_UserResponse)
    val w_usersTextNode = w_UserJSON.at("/data/ResourceUserIDs")
    val w_usersTextVal = w_usersTextNode.textValue()
    val w_usersData: JsonNode = JSONUtil.mapStringToJSON(w_usersTextVal)
    val w_userArray: JsonNode = w_usersData.at("/Users/User")
    var w_user: JsonNode = null

    if (w_userArray.isArray && w_userArray.iterator().hasNext) {
      w_user = w_userArray.iterator().next()
    } else {
      w_user = w_userArray
    }

    val w_email = if (w_user.hasNonNull("Email")) w_user.get("Email").textValue() else null
    val w_timeZone = if (w_user.hasNonNull("DateTime_Zone")) w_user.get("DateTime_Zone").textValue() else null
    val w_firstName = if (w_user.hasNonNull("First_Name")) w_user.get("First_Name").textValue() else ""
    val w_lastName = if (w_user.hasNonNull("Last_Name")) w_user.get("Last_Name").textValue() else ""
    val w_loginId = if(w_user.hasNonNull("Login_Id")) w_user.get("Login_Id").textValue()
    else throw new AppAppException(GlobalConstant.JSON_KEY_MISSING_NULL_VALUES + "loginId")

    val w_userDAO = new UserDAO {
      email = w_email
      enterpriseid = a_enterpriseDAO.getId
      firstname = w_firstName
      lastname = w_lastName
      loginid = w_loginId
      source_externaluserid = a_sourceExternalUserID
      source_userid = a_sourceUserID
      status = GlobalConstant.STATUS_OPEN
      timezone = w_timeZone
    }

    w_userDAO
  }
}