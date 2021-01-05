package com.app.framework.util.general

import java.util

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvSchema}
import com.app.core.crud.user.dataobject.dao.UserDAO
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

import scala.collection.mutable.ListBuffer

@RunWith(classOf[MockitoJUnitRunner])
class JTestJSONUtil extends AssertionsForJUnit {
  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testMapJSONToObject(): Unit = {
    val w_userJSON: JsonNode = JSONUtil.convertMapToJson(Map("id" -> "test", "source_userid" -> "userId"))
    val w_userDAO: Any = JSONUtil.mapJSONToObject(w_userJSON, classOf[UserDAO])

    assert(w_userDAO.isInstanceOf[UserDAO])
  }

  @Test
  def testMapStringToJSON(): Unit = {
    assertEquals(null, JSONUtil.mapStringToJSON(null))
    assert(JSONUtil.mapStringToJSON("{}").isInstanceOf[JsonNode])
  }

  @Test
  def testConvertMapToJson(): Unit = {
    assert(JSONUtil.convertMapToJson(Map("id" -> "test", "source_userid" -> "userId")).isInstanceOf[JsonNode])
  }

  @Test
  def testConvertUtilMapToJsonNode(): Unit = {
    assert( JSONUtil.convertUtilMapToJsonNode(new util.HashMap[String, Any]()).isInstanceOf[JsonNode] )
  }

  @Test
  def testConvertLinkedHashMapToJson(): Unit = {
    assert( JSONUtil.convertLinkedHashMapToJson(Map()).isInstanceOf[JsonNode] )
  }

  @Test
  def testGetJacksonObjectMapper(): Unit = {
    assert(JSONUtil.getJacksonObjectMapper.isInstanceOf[ObjectMapper])
  }

  @Test
  def testConvertListOfMapToListOfJsonNode(): Unit = {
    val w_list: util.List[util.HashMap[String, Any]] = new util.ArrayList[util.HashMap[String, Any]]()
    w_list.add(new util.HashMap[String, Any]())
    w_list.add(new util.HashMap[String, Any]())

    assert(JSONUtil.convertListOfMapToListOfJsonNode(w_list).length == 2)
  }

  @Test
  def testParseStringToJsonNode(): Unit = {
    assert(JSONUtil.parseStringToJsonNode("{}").isInstanceOf[JsonNode])
  }

  @Test
  def testGetJacksonCSVObjectMapper(): Unit = {
    assert(JSONUtil.getJacksonCSVObjectMapper.isInstanceOf[CsvMapper])
  }

  @Test
  def testGetCSVSchema(): Unit = {
    assert(JSONUtil.getCSVSchema.isInstanceOf[CsvSchema])
  }

  @Test
  def testConvertDAOToJSONNode(): Unit = {
    val w_userDAO: UserDAO = new UserDAO()

    assert(JSONUtil.convertDAOToJSONNode(w_userDAO).isInstanceOf[JsonNode])
  }

  @Test
  def testGetValueFromJSONNode(): Unit = {
    assertEquals("value", JSONUtil.getValueFromJSONNode("key", JSONUtil.parseStringToJsonNode("{\"key\":\"value\"}")))
    assertEquals(null, JSONUtil.getValueFromJSONNode("key", JSONUtil.parseStringToJsonNode("{\"key\":\"null\"}")))
    assertEquals(null, JSONUtil.getValueFromJSONNode("key", JSONUtil.parseStringToJsonNode("{\"key\":\"\"}")))
  }

  @Test
  def testMapJSONToObjectList(): Unit = {
    val w_jsonList: ListBuffer[JsonNode] = new ListBuffer()
    w_jsonList += JSONUtil.convertMapToJson(Map())
    w_jsonList += JSONUtil.convertMapToJson(Map())

    val w_daoList: ListBuffer[Any] = JSONUtil.mapJSONToObjectList(w_jsonList, classOf[UserDAO]).asInstanceOf[ListBuffer[Any]]

    w_daoList.foreach( userDAO => {
      assert(userDAO.isInstanceOf[UserDAO])
    } )
  }
}