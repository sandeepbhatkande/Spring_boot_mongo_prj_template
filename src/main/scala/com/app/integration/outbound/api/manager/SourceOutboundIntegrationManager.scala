package com.app.integration.outbound.api.manager

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO

trait SourceOutboundIntegrationManager {

  def synchronizeCardDataInSource(a_card: JsonNode, a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_itemType: String, a_loginId: String, a_projectId:String) : JsonNode

  def addAttachmentInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_itemType: String, a_itemCode: String, a_itemId : String,
                            a_attachmentFileName : String, a_attachmentFile : String, a_loginId: String, a_projectId:String, a_token: String) : Unit

  def addCommentsInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_activityLog: String, a_itemType: String, a_itemID: String, a_loginId: String,
                          a_projectId:String, a_token: String): Unit

  def sendMail (a_enterpriseDAO: EnterpriseDAO, a_to:String, a_subject:String, a_body:String, a_token: String) :Unit

}