package com.app.framework.util.app

import java.util

import com.app.framework.common.constant.GlobalConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import org.springframework.stereotype.Component

@Component
class QueryHelper {
	@Autowired  var m_template : MongoTemplate = _

	def read(a_criteriaMap: Map[String, Any], a_collectionName: String): util.List[util.HashMap[String, Any]] = {
		read(a_criteriaMap, a_collectionName, null)
	}

	def read(a_criteriaMap: Map[String, Any], a_collectionName: String, a_sortData: List[Map[String, String]]): util.List[util.HashMap[String, Any]] = {
		val w_dynamicQuery : Query = new Query()

		if ( a_criteriaMap != null ) {
			a_criteriaMap.keys.foreach(
				key => {
					val criteria = Criteria.where( key ).is( a_criteriaMap( key ) )
					w_dynamicQuery.addCriteria( criteria )
				}
			)
		}

		if ( a_sortData != null ) {
			a_sortData.foreach( sortData => {
				if ( GlobalConstant.SORT_ORDER_DESC.equals(sortData("order")) ) {
					w_dynamicQuery.`with`(new Sort( Sort.Direction.DESC, sortData("orderBy") ))
				} else {
					w_dynamicQuery.`with`(new Sort( Sort.Direction.ASC, sortData("orderBy") ))
				}
			} )
		}

		val w_result = m_template.find(w_dynamicQuery, classOf[java.util.HashMap[String, Any]], a_collectionName)

		processQueryResult( w_result )
	}

	def processQueryResult(a_result: util.List[util.HashMap[String, Any]]) : util.List[util.HashMap[String, Any]] = {
		if ( a_result != null )
			a_result.forEach(a_map => {
				a_map.put("id", a_map.get("_id").toString)
			})

		a_result
	}
}
