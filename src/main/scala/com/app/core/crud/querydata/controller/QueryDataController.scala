package com.app.core.crud.querydata.controller

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.querydata.service.QueryDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RestController}

@RestController(value = "querydatacontroller")
@RequestMapping(value = Array("/core/querydata"))
class QueryDataController() {
  @Autowired
  var m_service: QueryDataService = _

  @PostMapping(path = Array("/execute"))
  def execute(@RequestBody a_queryData: JsonNode): Map[String, Any] = {
    m_service.execute(a_queryData)
  }

  @PostMapping(path = Array("/encrypt"))
  def encryptPassword(@RequestBody a_loginData: JsonNode): Map[String, String] = {
    Map("password"-> m_service.encryptPassword(a_loginData.get("password").toString))
  }
}