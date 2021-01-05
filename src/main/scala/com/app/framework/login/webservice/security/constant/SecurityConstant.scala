package com.app.framework.login.webservice.security.constant

object SecurityConstant {
    
    val SECRET = "SecretKeyToGenJWTs"
    val EXPIRATION_TIME: Long = 86400000
    val TOKEN_PREFIX = "Bearer "
    val AUTH_HEADER_NAME = "Authorization"
    
    // Auth Mechanism
    val AUTH_FROM_DB = "DB"
}
