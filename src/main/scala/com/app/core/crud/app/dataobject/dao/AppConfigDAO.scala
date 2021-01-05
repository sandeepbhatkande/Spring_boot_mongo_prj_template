package com.app.core.crud.app.dataobject.dao

import com.app.core.crud.user.dataobject.dao.UserDAO
import com.app.framework.version.dataobject.dao.VersionDAO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document("app")
class AppConfigDAO {

  @Id
  @BeanProperty
  var projectId: String = _

  @BeanProperty
  var user_fullname: String = _

  @BeanProperty
  var enterpriseType: String = _

  @BeanProperty
  var helpURL: String = _

  @BeanProperty
  var user_data: UserDAO = _

  @BeanProperty
  var google_recaptcha_sitekey: String = _

  @BeanProperty
  var google_recaptcha_enabled: String = _

  @BeanProperty
  var versionInfo: VersionDAO = _
}