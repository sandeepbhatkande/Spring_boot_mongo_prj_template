package com.app.core.crud.user.helper

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.core.crud.user.repository.UserRepository
import com.app.framework.common.constant.GlobalConstant
import com.app.framework.exception.AppAppException
import com.app.framework.util.general.{DateUtil, WebUtil}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable.ArrayBuffer

@Component
class UserJsonHelper {
  @Autowired
  var m_repo: UserRepository = _

  def create(a_user: UserDAO): String = {
    try {
      a_user.setPassword(WebUtil.encryptString(GlobalConstant.DEFAULT_USER_SECRET))
      a_user.setCreationdate(DateUtil.getSysDate)
      m_repo.save(a_user).block().id
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }

  def read(a_id: String): UserDAO = {
    try {
      val w_result = m_repo.findById(a_id).block()

      if (w_result.isInstanceOf[UserDAO]) {
        w_result
      } else {
        null.asInstanceOf[UserDAO]
      }
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }

  def update(a_user: UserDAO): String = {
    try {
      a_user.setModifieddate(DateUtil.getSysDate)
      m_repo.save(a_user).block().id
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }

  def createDefaultUser(a_enterpriseID: String): Unit = {
    try {
      val w_userData = new UserDAO
      w_userData.enterpriseid = a_enterpriseID
      w_userData.loginid = a_enterpriseID + "_admin"
      w_userData.email = w_userData.loginid + "@issuecollector.com"
      w_userData.firstname = "system"
      w_userData.lastname = ""
      w_userData.timezone = "GMT"
      w_userData.status = GlobalConstant.STATUS_OPEN

      create(w_userData)
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }
  }
}