package com.app.framework.version.service

import com.app.framework.common.constant.DbConstants
import com.app.framework.version.dataobject.dao.VersionDAO
import com.app.framework.version.repository.VersionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service(value = "VersionService")
class VersionService {
  @Autowired
  var m_repo: VersionRepository = _
  @Autowired
  var m_template: MongoTemplate = _

  def getCurrentVersion: VersionDAO = {
    val w_query = new Query()

    w_query.`with`(new Sort( Sort.Direction.DESC, "creationdate" ))
    w_query.limit(1)

    m_template.findOne(w_query, classOf[VersionDAO], DbConstants.VERSION_INFO)
  }
}