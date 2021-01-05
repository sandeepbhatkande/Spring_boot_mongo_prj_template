package com.app.framework.util.general

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestFileUtil extends AssertionsForJUnit  {

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testGetResourceFilePath(): Unit = {
    assert(FileUtil.getResourceFilePath("/junit/data/TestFile.json") != null)
  }

  @Test
  def testGetResourceFile(): Unit = {
    assertEquals("{\n\n}", FileUtil.getResourceFile("/junit/data/TestFile.json"))
  }

  @Test
  def testGetJSONFromFilePath(): Unit = {
    assertEquals(JSONUtil.convertMapToJson(Map()), FileUtil.getJSONFromFilePath("/junit/data/TestFile.json"))
  }
}