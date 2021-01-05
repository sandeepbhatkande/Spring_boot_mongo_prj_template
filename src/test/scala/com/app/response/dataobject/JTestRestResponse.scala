package com.app.response.dataobject

import com.app.framework.response.dataobject.RestResponse
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.http.HttpStatus

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[MockitoJUnitRunner])
class JTestRestResponse extends AssertionsForJUnit {

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test def testRestReponse(): Unit = {
    val w_restResponse = new RestResponse("Success", ArrayBuffer("Success message"), Map(), HttpStatus.OK)

    assertEquals("Success", w_restResponse.respType)
  }
}