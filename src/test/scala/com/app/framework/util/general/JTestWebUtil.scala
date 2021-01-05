package com.app.framework.util.general

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestWebUtil extends AssertionsForJUnit {
  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testEncryptWithB64(): Unit = {
    assertEquals("MTExMTEx", WebUtil.encryptWithB64("111111"))
  }

  @Test
  def testDecryptWithB64(): Unit = {
    assertEquals("111111", WebUtil.decryptWithB64("MTExMTEx"))
  }

  @Test
  def testURLEncode(): Unit = {
    assertEquals("Test+String+%26+new+%24", WebUtil.urlEncode("Test String & new $"))
  }

  @Test
  def testSubstituteEscapeCharacters(): Unit = {
    assertEquals("Test%20String%20%26%20new%20%24%20ok%20%2B", WebUtil.substituteEscapeCharacters("Test String & new $ ok +"))
  }
}