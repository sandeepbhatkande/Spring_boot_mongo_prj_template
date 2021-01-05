package com.app.framework.util.app

import org.apache.logging.log4j.{Level, LogManager, Logger}

class MessageLogger {
    private var m_logger: Logger = _
    
    def getLogger(channelID: String) = new MessageLogger(channelID)
    
    def this(a_channelID: String) {
        this()
        m_logger = LogManager.getLogger(a_channelID)
    }
    
    // Common methods from log4j logging class
    //message = WebUtil.removeLF(message)
    def fatal(message: String): Unit = {
        m_logger.log(Level.FATAL, message)
    }
    
    def error(message: String): Unit = {
        m_logger.log(Level.ERROR, message)
    }
    
    def warn(message: String): Unit = {
        m_logger.log(Level.WARN, message)
    }
    
    def info(message: String): Unit = {
        m_logger.log(Level.INFO, message)
    }
    
    def debug(message: String): Unit = {
        m_logger.log(Level.DEBUG, message)
    }
}