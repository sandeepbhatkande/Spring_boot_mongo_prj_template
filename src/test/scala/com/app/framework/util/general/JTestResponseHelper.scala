package com.app.framework.util.general

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.http.HttpStatus

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[MockitoJUnitRunner])
class JTestResponseHelper extends AssertionsForJUnit {

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testGetResponse(): Unit = {
    val w_responseEntity = ResponseHelper.getResponse("Success", ArrayBuffer("Success message"), Map(), HttpStatus.OK)

    assertEquals(HttpStatus.OK, w_responseEntity.getStatusCode)
  }
}