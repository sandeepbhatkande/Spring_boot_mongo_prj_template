package com.app.framework.version.dataobject.dao

import java.util
import java.util.Date

import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document("versioninfo")
class VersionDAO {
  @BeanProperty
  var version: String = _

  @BeanProperty
  var creationdate: Date = _

  @BeanProperty
  var createdby: String = _

  @BeanProperty
  var modifieddate: Date = _

  @BeanProperty
  var modifiedby: String = _

  @BeanProperty
  var sourcesupporttedversion: util.HashMap[String, String] = _
}