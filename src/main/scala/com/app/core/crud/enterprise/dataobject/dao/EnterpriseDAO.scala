package com.app.core.crud.enterprise.dataobject.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document("enterprise")
class EnterpriseDAO {

  @Id
  @BeanProperty
  var id: String = _

  @BeanProperty
  var source_enterprisecode: String = _

  @BeanProperty
  var source_enterpriseid: Int = _

  @BeanProperty
  var source_url: String = _

  @BeanProperty
  var issuecollectoruiurl : String = _

  @BeanProperty
  var issuecollectordataurl : String = _

  @BeanProperty
  var enterprisetype: String = _

  @BeanProperty
  var name: String = _

  @BeanProperty
  var source_loginid: String = _

  @BeanProperty
  var source_password: String = _
}