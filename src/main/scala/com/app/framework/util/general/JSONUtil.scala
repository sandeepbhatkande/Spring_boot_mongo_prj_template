package com.app.framework.util.general

import java.util

import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, MapperFeature, ObjectMapper}
import com.fasterxml.jackson.dataformat.csv.CsvParser.Feature
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvSchema}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.app.framework.common.constant.GlobalConstant
import javax.servlet.ServletInputStream

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object JSONUtil {
	
	val m_objMapper = new ObjectMapper() with ScalaObjectMapper
	m_objMapper.registerModule(DefaultScalaModule)
	m_objMapper
		.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	
	val m_csvMapper = new CsvMapper
	m_csvMapper.registerModule(DefaultScalaModule)
	m_csvMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	m_csvMapper.configure(Feature.SKIP_EMPTY_LINES, true)
	m_csvMapper.registerModule(new JavaTimeModule)
	
	val m_csvSchema: CsvSchema = CsvSchema.builder.setUseHeader(true).build
	
	/**
	  * This api is used to read data object from JsonNode, used in getDTO()
	  *
	  * @param a_json JsonNode param
	  * @param a_class Class param
	  * @return
	  */
	def mapJSONToObject(a_json: JsonNode, a_class: Class[_]): Any = {
		
		getJacksonObjectMapper.treeToValue(a_json, a_class)
	}
	
	def mapJSONToObject(a_inputStream: ServletInputStream, a_class: Class[_]): Any = {
		
		getJacksonObjectMapper.readValue(a_inputStream, a_class)
	}
	
	def mapStringToJSON(a_json: String): JsonNode = {
		
		if (a_json != null)
			getJacksonObjectMapper.readTree(a_json)
		else
			null
	}
	
	def convertMapToJsonNode(a_map: mutable.HashMap[String, String]): JsonNode = {
		
		getJacksonObjectMapper.valueToTree(a_map)
	}

	def convertUtilMapToJsonNode(a_map: util.HashMap[String, Any]): JsonNode = {

		getJacksonObjectMapper.valueToTree(a_map)
	}
	
	def convertMapToJson(a_map: collection.Map[String, Any]): JsonNode = {
		
		getJacksonObjectMapper.valueToTree(a_map)
	}

  def convertLinkedHashMapToJson(a_map: Any): JsonNode = {

    getJacksonObjectMapper.valueToTree(a_map)
  }
	
	def getJacksonObjectMapper: ObjectMapper = {
		
		m_objMapper
	}

	def convertListOfMapToListOfJsonNode(a_list: util.List[util.HashMap[String, Any]]): ListBuffer[JsonNode] = {
		val w_list : ListBuffer[JsonNode] =  new ListBuffer[JsonNode]()

		a_list.forEach( a_map => {
			val w_json = convertLinkedHashMapToJson( a_map )
			w_list += w_json
		} )

		w_list
	}

  def parseStringToJsonNode (a_inputStr:String): JsonNode = {
    val mapper = new ObjectMapper
    mapper.readTree(a_inputStr)
  }

	def getJacksonCSVObjectMapper: CsvMapper = {
		
		m_csvMapper
	}
	
	def getCSVSchema: CsvSchema = {
		
		m_csvSchema
	}
	
	def getSubsetMap(a_parent: mutable.HashMap[String, Any], a_childList: ArrayBuffer[String]): collection.Map[String, Any] = {
		
		a_parent filterKeys a_childList.toSet
	}
	
	def convertDAOToJSONNode(a_object: Any): JsonNode = {
		val mapper = new ObjectMapper
		val a : JsonNode = mapper.valueToTree(a_object)
		a
	}
	
	def getValueFromJSONNode(a_strFromJson: String, a_jsonNode: JsonNode): String = {
		
		var w_jsonText: String = null

		if (a_jsonNode != null) {
			val w_jsonNode = a_jsonNode.get(a_strFromJson)
			
			if (w_jsonNode != null)
				w_jsonText = w_jsonNode.textValue()
			
			if ( "null".equalsIgnoreCase(w_jsonText) || "".equalsIgnoreCase(w_jsonText) )
				w_jsonText = null
		}

		w_jsonText
	}
	
	def imposeOnMap(a_map: mutable.HashMap[String, Any], a_jsonToBeImposed: JsonNode,
    a_jsonKeyStr: String, a_jsonValueStr: String): mutable.HashMap[String, Any] = {
		
		var a_imposedMap = a_map

		//Need to review this code it has issues
		if (a_jsonToBeImposed.get(0).isArray) {
			a_jsonToBeImposed.get(0).forEach(w_feature => {
				a_imposedMap += (w_feature.get(a_jsonKeyStr).asText -> w_feature.get(a_jsonValueStr).asText)
			})
		} else {
			a_jsonToBeImposed.forEach(w_feature => {
				a_imposedMap += (w_feature.get(a_jsonKeyStr).asText -> w_feature.get(a_jsonValueStr).asText)
			})
		}
		
		a_imposedMap
	}
	
  def mapJSONToObjectList(a_json: ListBuffer[JsonNode], a_class: Class[_]): Any = {
    val w_list : ListBuffer[Any] =  new ListBuffer[Any]()

    for (i <- GlobalConstant.DEFAULT_LOOP_INITIALIZER until a_json.size) {
      val w_dataObj: Any = getJacksonObjectMapper.treeToValue(a_json(i), a_class)

      w_list += w_dataObj
    }

    w_list
  }
}