package com.app.core.crud.querydata.service

import java.util.Date

import com.fasterxml.jackson.databind.JsonNode
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoDatabase
import com.app.framework.util.general.{DateUtil, JSONUtil, WebUtil}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

import scala.collection.mutable.ListBuffer

@Service(value = "QueryDataService")
class QueryDataService() {
  @Autowired var m_template : MongoTemplate = _

  def execute(a_queryData: JsonNode): Map[String, Any] = {
    var w_results: ListBuffer[JsonNode] = ListBuffer()

    val w_db: MongoDatabase = m_template.getDb
    val w_collection = w_db.getCollection(a_queryData.get("collection").asText)
    val w_query : BasicDBObject = BasicDBObject.parse(a_queryData.get("query").toString)
    val w_queryStartTime : Date = DateUtil.getSysDate
    val w_data = w_collection.find(w_query)
    val w_queryEndTime : Date = DateUtil.getSysDate

    val w_iterator = w_data.iterator()

    while (w_iterator.hasNext) {
      val w_document = w_iterator.next

      w_results += JSONUtil.mapStringToJSON(w_document.toJson)
    }

    val w_queryExecutionTime = w_queryEndTime.getTime - w_queryStartTime.getTime

    Map("QueryExecutionTime" -> w_queryExecutionTime, "results" -> w_results)
  }

  def encryptPassword(a_password: String) : String = {
      WebUtil.encryptWithB64(a_password)
  }
}