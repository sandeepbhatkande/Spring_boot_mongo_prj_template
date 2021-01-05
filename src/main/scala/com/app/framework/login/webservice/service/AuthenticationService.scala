package com.app.framework.login.webservice.service

import java.util

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.crud.user.dataobject.dto.UserAuthData
import com.app.core.crud.user.service.UserService
import com.app.framework.common.constant.GlobalConstant
import com.app.framework.util.app.QueryHelper
import com.app.framework.util.general.JSONUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}
import org.springframework.stereotype.Service

@Service
class AuthenticationService(@Autowired m_userService: UserService, @Autowired m_queryHelper: QueryHelper) extends UserDetailsService {
    
    @throws[UsernameNotFoundException]
    override def loadUserByUsername(a_entIDAndLoginID: String): UserDetails = {
        
        val w_userData = preLoginUserValidate(a_entIDAndLoginID)
        
        buildUserAuthData(w_userData)
    }

    private def preLoginUserValidate(a_entIDAndLoginID: String): UserDAO = {

      val w_entIDLoginID = a_entIDAndLoginID.split("###")
      val w_enterpriseID = w_entIDLoginID(0)
      val w_loginID = w_entIDLoginID(1)

      val w_criteriaMap = Map("enterpriseid" -> w_enterpriseID, "loginid" -> w_loginID, "status" -> GlobalConstant.STATUS_OPEN)
      val w_userList : util.List[util.HashMap[String, Any]] = m_queryHelper.read(w_criteriaMap, "user")

      if (w_userList == null)
          throw new UsernameNotFoundException(a_entIDAndLoginID)

      val w_userJSONNode = JSONUtil.convertListOfMapToListOfJsonNode(w_userList)
      var w_user: UserDAO = null

      if (w_userJSONNode.nonEmpty)
        w_user = JSONUtil.mapJSONToObject(w_userJSONNode.head, classOf[UserDAO]).asInstanceOf[UserDAO]

      w_user
    }
    
    private def buildUserAuthData(a_userData: UserDAO): UserAuthData = {
        
        val w_userAuthData = new UserAuthData(a_userData.getLoginid, a_userData.getPassword, new java.util.ArrayList[GrantedAuthority])
        w_userAuthData.setEnterpriseID(a_userData.getEnterpriseid)
        w_userAuthData.setLoginID(a_userData.getLoginid)
        
        w_userAuthData
    }
}
