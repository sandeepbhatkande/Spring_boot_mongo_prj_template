package com.app.framework.exception

import scala.collection.mutable.ArrayBuffer

class AppAppException(message: String) extends Exception(message) {
    
    var m_exceptionType: String = _ // The type of Exception. i.e. Can be a Warning or Error
    var m_exceptionDesc: String = _
    var m_stackTraceStr: String = _
    var m_errorList: ArrayBuffer[String] = _
    
    def this(a_message: String, a_cause: Throwable) {
        this(a_message)
        initCause(a_cause)
    }
    
    def this(a_cause: Throwable) {
        this(Option(a_cause).map(_.toString).orNull, a_cause)
    }
    
    def this() {
        this(null: String)
    }
    
    def this(a_message: String, a_exceptionType: String, a_exceptionDesc: String, a_StackTrace: String) {
        this(a_message)

        m_exceptionDesc = a_exceptionDesc
        m_exceptionType = a_exceptionType
        m_stackTraceStr = a_StackTrace
    }
    
    def this(a_exceptionType: String, a_message: String, a_errList: ArrayBuffer[String]) {
        this(a_message, null)

        m_exceptionType = a_exceptionType
        m_errorList = a_errList
    }
    
    def getExceptionDescription: String = m_exceptionDesc
}