package com.app.framework.util.general

import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

import scala.collection.mutable

@RunWith(classOf[MockitoJUnitRunner])
class JTestCollectionUtil extends AssertionsForJUnit {
  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testConvertHashMapToKeyValueString(): Unit = {
    val w_map: mutable.HashMap[Object, Object] = new mutable.HashMap[Object, Object]()
    w_map.put("key", "value")

    assertEquals(null, CollectionUtil.convertHashMapToKeyValueString(null))
    assertEquals("key:value ", CollectionUtil.convertHashMapToKeyValueString(w_map))
  }
}