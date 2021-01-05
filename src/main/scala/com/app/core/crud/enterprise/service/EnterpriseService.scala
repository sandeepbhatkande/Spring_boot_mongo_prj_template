package com.app.core.crud.enterprise.service

import java.util

import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.core.crud.enterprise.repository.EnterpriseRepository
import com.app.core.crud.user.service.UserService
import com.app.framework.common.constant.DbConstants
import com.app.framework.util.app.QueryHelper
import com.app.framework.util.general.JSONUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "EnterpriseService")
class EnterpriseService(@Autowired m_repo: EnterpriseRepository, @Autowired m_userService : UserService,  @Autowired m_queryHelper : QueryHelper) {

  def create(a_enterprise: EnterpriseDAO): String = {
    var w_enterpriseID: String = ""
    val w_entDAO = getEnterpriseBySrcIdAndCode(a_enterprise)

    if ( w_entDAO.id != null ) {
      a_enterprise.id = w_entDAO.id
      w_enterpriseID = update(a_enterprise)
   } else {
      w_enterpriseID = m_repo.save(a_enterprise).block().id
      m_userService.createDefaultUser(w_enterpriseID)
    }

    w_enterpriseID
  }

  def read(a_id: String): EnterpriseDAO = {
    val w_result = m_repo.findById(a_id).block()

    if (w_result.isInstanceOf[EnterpriseDAO]) {
      w_result
    } else {
      null.asInstanceOf[EnterpriseDAO]
    }
  }

  def update(a_enterprise: EnterpriseDAO): String = {
    m_repo.save(a_enterprise).block().id
  }

  def getEnterpriseBySrcIdAndCode(a_enterprise: EnterpriseDAO) : EnterpriseDAO ={
    val w_src_EntCode: String = a_enterprise.source_enterprisecode
    val w_src_EntId: Int = a_enterprise.source_enterpriseid

    val w_criteriaMap = Map("source_enterprisecode" -> w_src_EntCode, "source_enterpriseid" -> w_src_EntId)
    val w_entList  : util.List[util.HashMap[String, Any]] = m_queryHelper.read(w_criteriaMap, DbConstants.ENTERPRISE_COLLECTION)
    val w_entJSONNode = JSONUtil.convertListOfMapToListOfJsonNode(w_entList)
    val w_ent: EnterpriseDAO = if ( w_entJSONNode.nonEmpty ) JSONUtil.mapJSONToObject(w_entJSONNode.head, classOf[EnterpriseDAO]).asInstanceOf[EnterpriseDAO] else new EnterpriseDAO()

    w_ent
  }
}