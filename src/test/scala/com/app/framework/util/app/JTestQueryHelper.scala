package com.app.framework.util.app

import java.util

import com.app.framework.common.constant.GlobalConstant
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.{InjectMocks, Mock, MockitoAnnotations}
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query

@RunWith(classOf[MockitoJUnitRunner])
class JTestQueryHelper extends AssertionsForJUnit {

  @Mock
  var m_template: MongoTemplate = _

  @InjectMocks
  var m_queryHelper: QueryHelper = _

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testRead(): Unit = {
    val w_criteriaMap: Map[String, Any] = Map("key" -> "value")
    val w_collectionName: String = "collection"

    when(m_template.find(any(classOf[Query]), any(), any(classOf[String]))).thenReturn(null)

    assertEquals(null, m_queryHelper.read(w_criteriaMap, w_collectionName))
  }

  @Test
  def testRead1(): Unit = {
    val w_criteriaMap: Map[String, Any] = Map("key" -> "value")
    val w_collectionName: String = "collection"
    val w_sortData: List[Map[String, String]] = List(Map("order" -> GlobalConstant.SORT_ORDER_DESC, "orderBy" -> "desc"), Map("order" -> GlobalConstant.SORT_ORDER_ASC, "orderBy" -> "asc"))

    when(m_template.find(any(classOf[Query]), any(), any(classOf[String]))).thenReturn(null)


    assertEquals(null, m_queryHelper.read(w_criteriaMap, w_collectionName, w_sortData))
  }

  @Test
  def testProcessQueryResult(): Unit = {
    val w_queryData: util.List[util.HashMap[String, Any]] = new util.ArrayList[util.HashMap[String, Any]]()

    val w_data = new util.HashMap[String, Any]()
    w_data.put("_id", "testID")

    w_queryData.add(w_data)

    val w_actual: util.List[util.HashMap[String, Any]] = m_queryHelper.processQueryResult(w_queryData)

    assertEquals("testID", w_actual.get(0).get("id"))
  }
}