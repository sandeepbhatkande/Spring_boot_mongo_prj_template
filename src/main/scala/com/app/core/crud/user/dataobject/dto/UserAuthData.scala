package com.app.core.crud.user.dataobject.dto

import java.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

import scala.beans.BeanProperty

class UserAuthData(a_userName: String, a_password: String, a_arrayBuffer: util.ArrayList[GrantedAuthority])
    extends User(a_userName: String, a_password: String, a_arrayBuffer: util.ArrayList[GrantedAuthority]) {
    
    @BeanProperty
    var enterpriseID: String = _
    @BeanProperty
    var loginID: String = _
    @BeanProperty
    var authToken: String = _

    def this() {
        this("test", "welcome@1", new util.ArrayList[GrantedAuthority])
    }

    def getUniqueKey: String = {

        enterpriseID + "###" + loginID
    }
}

object UserAuthData {
    
    val UNIQUE_KEY_SEPARATOR = "###"
}