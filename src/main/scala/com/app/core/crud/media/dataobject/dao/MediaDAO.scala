package com.app.core.crud.media.dataobject.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document("media")
class MediaDAO {
  @Id
  @BeanProperty
  var id: String = _

  @BeanProperty
  var image: Array[Byte] = _

  @BeanProperty
  var enterpriseid: String = _

  @BeanProperty
  var mimetype: String = _
}