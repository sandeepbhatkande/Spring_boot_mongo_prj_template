package com.app.framework.util.general

import com.app.framework.response.dataobject.RestResponse
import org.springframework.http.{HttpStatus, ResponseEntity}

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

object ResponseHelper {

  def getResponse(respType: String, respMessage: ArrayBuffer[String], respData: Map[String, Any],  responseCode: HttpStatus): ResponseEntity[Object] ={

    val restResponse: RestResponse = new RestResponse(respType, respMessage, respData, responseCode)
    restResponse.responseEntity
  }
}