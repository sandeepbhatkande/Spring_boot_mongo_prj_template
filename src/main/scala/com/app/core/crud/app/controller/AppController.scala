package com.app.core.crud.app.controller

import com.app.core.crud.app.dataobject.dao.AppConfigDAO
import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.app.service.AppService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController(value = "appcontroller")
@RequestMapping(value = Array("/core/app"))
class AppController(@Autowired m_service: AppService)
{
  @PostMapping(path = Array("/getConfig/{enterpriseID}/{sourcePrjId}/{externalPrjId}"))
  def read(@RequestBody postparams:JsonNode, @PathVariable("enterpriseID") a_enterpriseId: String, @PathVariable("sourcePrjId") a_sourcePrjId: Int,
           @PathVariable("externalPrjId") a_externalPrjId: String): AppConfigDAO = {
    val w_soLoginId:String = if(postparams.get("sologinid") != null) postparams.get("sologinid").asText else null
    m_service.getConfig( a_enterpriseId, a_sourcePrjId, a_externalPrjId, postparams.get("sointusrid").asText, postparams.get("soexturid").asText, w_soLoginId )
  }

}