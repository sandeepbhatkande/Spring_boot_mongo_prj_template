package com.app.core.crud.user.service

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.crud.user.helper.UserJsonHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "UserService")
class UserService() {
  @Autowired
  var m_serviceHelper: UserJsonHelper = _

  def create(a_user: UserDAO): String = {
    m_serviceHelper.create(a_user)
  }

  def read(a_id: String): UserDAO = {
    m_serviceHelper.read(a_id)
  }

  def update(a_user: UserDAO): String = {
    m_serviceHelper.update(a_user)
  }

  def createDefaultUser(a_enterpriseID : String) : Unit = {
    m_serviceHelper.createDefaultUser(a_enterpriseID)
  }
}