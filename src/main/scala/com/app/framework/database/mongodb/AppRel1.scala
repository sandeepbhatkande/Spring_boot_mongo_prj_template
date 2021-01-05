package com.app.framework.database.mongodb

import java.util

import com.app.framework.common.constant.DbConstants
import com.app.framework.util.general.DateUtil
import com.app.framework.version.dataobject.dao.VersionDAO
import com.app.integration.inbound.constant.IntegrationConstant
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mongobee.changeset.{ChangeLog, ChangeSet}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

@ChangeLog(order = "1")
class AppRel1
{
  @Autowired var objectMapper: ObjectMapper = _

  @ChangeSet(order = "101235", id="101235", author = "jshaikh")
  def createVersionInfoCollection(a_mongoTemplate : MongoTemplate): Unit = {
    a_mongoTemplate.createCollection(DbConstants.VERSION_INFO)

    val w_sourceSupportedVersions: util.HashMap[String, String] = new util.HashMap[String, String]()
    w_sourceSupportedVersions.put(IntegrationConstant.SWIFTENTERPRISE, "6.13")
    w_sourceSupportedVersions.put(IntegrationConstant.SWIFTKANBAN, "7.15")
    w_sourceSupportedVersions.put(IntegrationConstant.SWIFTEASE, "4.15")

    a_mongoTemplate.insert(
      new VersionDAO {
        version = "1.0"
        creationdate = DateUtil.getSysDate
        createdby = "Jishan Shaikh"
        sourcesupporttedversion = w_sourceSupportedVersions
      }, DbConstants.VERSION_INFO)
  }
}