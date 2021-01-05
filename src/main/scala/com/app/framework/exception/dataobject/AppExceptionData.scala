package com.app.framework.exception.dataobject

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

import scala.collection.mutable.ArrayBuffer

class AppExceptionData(val message: String, val params: List[String]) {
    
    @JsonProperty("stacktrace")
    var m_stacktrace: String = _
    var m_errorStatus: HttpStatus = _
    var m_errorList: ArrayBuffer[String] = _
    
    def this(a_errorStatus: HttpStatus, a_message: String, a_params: List[String], a_stacktraceStr: String) {
        this(a_message, a_params)
        m_stacktrace = a_stacktraceStr
        m_errorStatus = a_errorStatus
    }
    
    def this(a_errorStatus: HttpStatus, a_message: String, a_params: List[String]) {
        this(a_message, a_params)
        m_errorStatus = a_errorStatus
    }
    
    def this(a_errorStatus: HttpStatus, a_message: String, a_params: List[String], a_errorList: ArrayBuffer[String]) {
        this(a_message, a_params)
        m_errorStatus = a_errorStatus
        m_errorList = a_errorList
    }
}
