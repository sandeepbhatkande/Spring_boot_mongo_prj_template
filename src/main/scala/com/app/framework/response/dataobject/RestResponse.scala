package com.app.framework.response.dataobject

import com.fasterxml.jackson.databind.JsonNode
import com.app.framework.util.general.JSONUtil
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

class RestResponse (val respType: String, val respMessage: ArrayBuffer[String], val respData: Map[String, Any], val respCode: HttpStatus){
  var responseEntity: ResponseEntity[Object] = _

  var w_RespMap: Map[String, Any] = Map()
  var w_messageViewMap: Map[String, Any] = Map()

  w_RespMap += ("details" -> respData)
  w_messageViewMap += ("type" -> respType)
  w_messageViewMap += ("message" -> respMessage)
  w_RespMap += ("messageView" -> w_messageViewMap)

  var w_respJson: JsonNode =  JSONUtil.convertMapToJson(w_RespMap)
  responseEntity = new ResponseEntity[Object](w_respJson, new HttpHeaders, respCode)
}