package com.app.framework.util.general

import java.io.{BufferedReader, InputStreamReader}

import com.fasterxml.jackson.databind.JsonNode

object FileUtil {

	/**
		* This api is used to read actual resource file path
		*
		* @param a_filePath String param
		* @return
		*/
	def getResourceFilePath(a_filePath: String): String = {
		getClass.getResource(a_filePath).getPath
	}

	/**
		* This api is used to read resource file
		*
		* @param a_filePath String param
		* @return
		*/
	def getResourceFile(a_filePath: String): String = {
		val reader: BufferedReader = new BufferedReader(new InputStreamReader(getClass.getResourceAsStream(a_filePath)))
		val w_data: String = Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")

		reader.close()

		w_data
	}

	/**
		* This api is used to get JSON form resource file path
		*
		* @param a_filePath String param
		* @return
		*/
	def getJSONFromFilePath(a_filePath: String) : JsonNode = {
		JSONUtil.mapStringToJSON(getResourceFile(a_filePath))
	}
}