package com.app.framework.util.app

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestApplicationProperties extends AssertionsForJUnit {
  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test def testAppProps() : Unit = {
    val w_appProps = new ApplicationProperties()

    assertEquals(864000000, w_appProps.JWT_EXPIRATION_TIME)
  }
}