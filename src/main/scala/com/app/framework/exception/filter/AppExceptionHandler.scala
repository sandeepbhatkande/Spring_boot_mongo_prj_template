package com.app.framework.exception.filter

import com.app.framework.exception.AppAppException
import com.app.framework.exception.dataobject.AppExceptionData
import com.app.framework.util.app.MessageLogger
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ExceptionHandler, RestControllerAdvice}
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import scala.collection.mutable

@RestControllerAdvice(basePackages = Array("com.issuecollector"))
class AppExceptionHandler extends ResponseEntityExceptionHandler {
    
    private val m_logger = new MessageLogger(this.getClass.getName)
    
    /**
      * This api intercepts AppException and creates ExceptionData object, populates the data and returns the same
      *
      * @param a_ex      - AppException
      * @param a_request - WebRequest
      * @return
      */
    @ExceptionHandler(Array(classOf[AppAppException]))
    def handleAppException(a_ex: AppAppException, a_request: WebRequest): ResponseEntity[Object] = {
        m_logger.error("AppException occurred : " + a_ex.getMessage)
        //TODO: set stacktrace on flag
        /*val w_errorData = new AppExceptionData(HttpStatus.INTERNAL_SERVER_ERROR, a_ex.getMessage,
            getRequestParams(a_request), WebUtil.getExceptionStacktrace(a_ex))*/
        val w_errorData = new AppExceptionData(HttpStatus.BAD_REQUEST, a_ex.getMessage,
            getRequestParams(a_request), a_ex.m_errorList)
        
        new ResponseEntity[Object](w_errorData, new HttpHeaders, w_errorData.m_errorStatus)
    }
    
    /**
      * This api returns list of request parameters in key:value form from request object
      *
      * @param a_request - WebRequest
      * @return
      */
    def getRequestParams(a_request: WebRequest): List[String] = {
        val w_enum = a_request.getParameterNames
        val w_paramList = new mutable.ArrayBuffer[String]
        while (w_enum.hasNext) {
            val w_name = w_enum.next
            val w_value = a_request.getParameter(w_name)
            w_paramList += (w_name + ":" + w_value)
        }
        w_paramList.toList
    }
    
    /**
      * This api intercepts Exception and creates ExceptionData object, populates the data and returns the same
      *
      * @param a_ex: Exception
      * @param a_request: WebRequest
      * @return
      */
    @ExceptionHandler(Array(classOf[Exception]))
    def handleAll(a_ex: Exception, a_request: WebRequest): ResponseEntity[Object] = {
        m_logger.error("Exception occurred : " + a_ex.getMessage)
        //TODO: set stacktrace on flag
        /*val w_errorData = new AppExceptionData(HttpStatus.INTERNAL_SERVER_ERROR, a_ex.getMessage, getRequestParams(a_request)
            , WebUtil.getExceptionStacktrace(a_ex))*/
        val w_errorData = new AppExceptionData(HttpStatus.INTERNAL_SERVER_ERROR, a_ex.getMessage, getRequestParams(a_request)
            , a_ex.getLocalizedMessage)
        new ResponseEntity[Object](w_errorData, new HttpHeaders, w_errorData.m_errorStatus)
    }
    
    /**
      *
      * @param a_ex: NoHandlerFoundException
      * @param a_headers: HttpHeaders
      * @param a_status: HttpStatus
      * @param a_request: WebRequest
      * @return ResponseEntity
      */
    override protected def handleNoHandlerFoundException(a_ex: NoHandlerFoundException, a_headers: HttpHeaders, a_status: HttpStatus, a_request: WebRequest)
    : ResponseEntity[Object] = {
        val w_errorData: AppExceptionData = new AppExceptionData(HttpStatus.NOT_FOUND, a_ex.getMessage,
            getRequestParams(a_request))
        new ResponseEntity[Object](w_errorData, new HttpHeaders(), w_errorData.m_errorStatus)
    }
}
