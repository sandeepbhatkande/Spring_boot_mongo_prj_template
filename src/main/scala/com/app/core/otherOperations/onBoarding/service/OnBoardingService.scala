package com.app.core.otherOperations.onBoarding.service

import com.app.core.otherOperations.onBoarding.bean.OnBoardingManagerBean
import com.app.framework.util.general.{ResponseHelper, WebUtil}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.stereotype.Service

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

@Service(value = "OnBoardingService")
class OnBoardingService(@Autowired m_boardBean: OnBoardingManagerBean) {

  def onBoardingProcess(entId: String, prjId: Int, extPrjId: String, userId: String, extUserId: String, loginId: String): ResponseEntity[Object] = {
    var a_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
    var a_respMsg: ArrayBuffer[String] = new ArrayBuffer[String]()
    var a_boardRespMap: Map[String, Any] = Map()

    try {
      a_boardRespMap = m_boardBean.onBoardingProcess(entId, prjId, extPrjId, userId, extUserId, loginId)

      a_respMsg += "App Data added successfully"

      ResponseHelper.getResponse("Success", a_respMsg, a_boardRespMap, HttpStatus.OK)
    }
    catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        a_errorList += e.getMessage

        ResponseHelper.getResponse("Error", a_errorList, a_boardRespMap, HttpStatus.UNPROCESSABLE_ENTITY)
    }
  }
}