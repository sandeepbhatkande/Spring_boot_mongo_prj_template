package com.app.framework.login.webservice.service

import java.util

import com.app.core.crud.user.service.UserService
import com.app.framework.util.app.QueryHelper
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.{InjectMocks, Mock, MockitoAnnotations}
import org.scalatest.{FlatSpec, PrivateMethodTester}

@RunWith(classOf[MockitoJUnitRunner])
class JTestAuthenticationService extends FlatSpec with PrivateMethodTester {

  @Mock
  var m_userService: UserService = _
  @Mock
  var m_queryHelper: QueryHelper = _

  @InjectMocks
  var m_authenticationService: AuthenticationService = _

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testPreLoginUserValidate(): Unit = {
    val w_entAndLoginId: String = "ent###loginId"

    val w_userDataList: util.List[util.HashMap[String, Any]] = new util.ArrayList[util.HashMap[String, Any]]()
    val w_userData: util.HashMap[String, Any] = new util.HashMap[String, Any]()
    w_userData.put("id", "abc")

    w_userDataList.add(w_userData)

    val w_plsv = PrivateMethod[AuthenticationService]('preLoginUserValidate)

    assert((m_authenticationService invokePrivate w_plsv(w_entAndLoginId)) == null)
  }
}