package com.app.integration.inbound.api.manager

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.user.dataobject.dao.UserDAO

trait SourceInboundIntegrationManager {

  def getSourceUserDetails(a_enterpriseDAO : EnterpriseDAO, a_sourceUserID : String, a_sourceExternalUserID : String, a_loginID : String, a_token : String) : UserDAO
}
