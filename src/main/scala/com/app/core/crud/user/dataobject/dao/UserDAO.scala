package com.app.core.crud.user.dataobject.dao

import java.util.Date

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document("user")
class UserDAO {

  @Id
  @BeanProperty
  var id: String = _

  @BeanProperty
  var source_userid : String = _

  @BeanProperty
  var source_externaluserid : String = _

  @BeanProperty
  var loginid : String = _

  @BeanProperty
  var firstname : String = _

  @BeanProperty
  var lastname : String = _

  @BeanProperty
  var email : String = _

  @BeanProperty
  var timezone : String = _

  @BeanProperty
  var enterpriseid : String = _

  @BeanProperty
  var status : String = _

  @BeanProperty
  var password : String = _

  @BeanProperty
  var creationdate: Date = _

  @BeanProperty
  var modifieddate: Date = _

  def readFullName(): String = {
    firstname + " " + lastname
  }

  override def toString: String = s"UserDAO(id=$id, source_userid=$source_userid, "+
    s"source_externaluserid=$source_externaluserid, loginid=$loginid, " +
    s"firstname=$firstname, lastname=$lastname, email=$email, timezone=$timezone, " +
    s"enterpriseid=$enterpriseid, status=$status, " +
    s"creationdate=$creationdate, modifieddate=$modifieddate)"
}