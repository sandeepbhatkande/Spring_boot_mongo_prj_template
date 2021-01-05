package com.app.integration.outbound.api.bean

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.framework.common.constant.GlobalConstant
import com.app.framework.util.app.{I18N, QueryHelper}
import com.app.framework.util.general.{JSONUtil, StringUtil, WebUtil}
import com.app.integration.inbound.constant.IntegrationConstant
import com.app.integration.inbound.helper.InboundIntegrationHelper
import com.app.integration.outbound.api.manager.SourceOutboundIntegrationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.immutable.Map
import scala.collection.mutable

@Component
class SESourceOutboundIntegrationManagerBean extends SourceOutboundIntegrationManager {

  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  @Autowired
  var m_queryHelper: QueryHelper = _

  override def synchronizeCardDataInSource(a_card: JsonNode, a_enterpriseDAO: EnterpriseDAO,
    a_projectCode: String, a_itemType: String, a_loginId: String, a_projectId: String): JsonNode = {

    val w_name = if (a_card.hasNonNull(GlobalConstant.CARDNAME)) a_card.get(GlobalConstant.CARDNAME).asText else null
    val w_description = if (a_card.hasNonNull(GlobalConstant.DESCRIPTION)) a_card.get(GlobalConstant.DESCRIPTION).asText else null
    val w_customFields = if (a_card.hasNonNull("customFields")) a_card.get("customFields") else null
    val w_EFormSyncRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
      IntegrationConstant.SWIFTENTERPRISE_REST_EFORM_CREATE_DETAILS_URL

    val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
    val w_methodType: String = IntegrationConstant.METHOD_POST

    val w_fieldLabels = new StringBuilder()
    val w_fieldValues = new StringBuilder()

    if (w_name != null) {
      w_fieldLabels.append(IntegrationConstant.SWIFTENTERPRISE_EFORM_NAME).append(GlobalConstant.COMMA)
      w_fieldValues.append(StringUtil.replaceString(w_name, GlobalConstant.COMMA, IntegrationConstant.SWIFTENTERPRISE_ESCAPE_COMMA)).append(GlobalConstant.COMMA)
    }

    if (w_description != null) {
      w_fieldLabels.append(IntegrationConstant.SWIFTENTERPRISE_EFORM_DESCRIPTION).append(GlobalConstant.COMMA)
      w_fieldValues.append(StringUtil.replaceString(w_description, GlobalConstant.COMMA, IntegrationConstant.SWIFTENTERPRISE_ESCAPE_COMMA)).append(GlobalConstant.COMMA)
    }

    if (w_customFields != null) {
      w_customFields.forEach(obj => {
        w_fieldLabels.append(obj.get("fieldLabel").asText).append(GlobalConstant.COMMA)
        w_fieldValues.append(StringUtil.replaceString(obj.get("value").asText, GlobalConstant.COMMA, IntegrationConstant.SWIFTENTERPRISE_ESCAPE_COMMA)).append(GlobalConstant.COMMA)
      })
    }

    val w_attachmentFileName = if (a_card.hasNonNull(GlobalConstant.ATTACHMENT_NAME)) a_card.get(GlobalConstant.ATTACHMENT_NAME).asText else null
    val w_attachmentFile = if (a_card.hasNonNull(GlobalConstant.ATTACHMENT_FILE)) a_card.get(GlobalConstant.ATTACHMENT_FILE).asText else null

    if (w_attachmentFileName != null && w_attachmentFile != null)
    {
      w_fieldLabels.append(IntegrationConstant.SWIFTENTERPRISE_ISSUE_COLLECTOR_FILE_NAME).append(GlobalConstant.COMMA)
      w_fieldValues.append(StringUtil.replaceString(w_attachmentFileName, GlobalConstant.COMMA, IntegrationConstant.SWIFTENTERPRISE_ESCAPE_COMMA)).append(GlobalConstant.COMMA)
    }

    w_fieldLabels.append(IntegrationConstant.ISSUE_COLLECTOR_REST_EFORM_UPDATE_CALL)
    w_fieldValues.append(GlobalConstant.YES)

    var w_dataMap: Map[String, String] = Map.empty
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_FIELD_LABELS -> w_fieldLabels.toString)
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_FIELD_VALUES -> w_fieldValues.toString)
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_CREATOR_LOGINID -> a_loginId)
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE -> IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE_PROJECT)
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERCODE -> a_projectCode)
    w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ITEMTYPE -> a_itemType)

    val w_dataJSON = JSONUtil.convertMapToJson(w_dataMap)

    val w_token = m_inboundIntegrationHelper.getTokenDetails(a_enterpriseDAO)

    val w_EFormResponse = m_inboundIntegrationHelper.getRestAPIResponseForSwiftEnterprise(w_EFormSyncRESTURL, w_contentType,
      w_methodType, w_dataJSON.toString, a_enterpriseDAO, w_token)

    if ( w_EFormResponse != null ) {
      val w_JSONNode = JSONUtil.mapStringToJSON(w_EFormResponse)

      val w_itemIDAndItemCode : String = w_JSONNode.at("/data/ItemCode").textValue()
      val w_itemCodeItemID = w_itemIDAndItemCode.split("--")
      val w_itemId = w_itemCodeItemID(0)
      val w_itemcode = w_itemCodeItemID(1)

      val w_respMap: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]
      w_respMap += ("ItemCode" -> w_itemcode)
      w_respMap += ("itemType" -> a_itemType)
      w_respMap += ("itemId" -> w_itemId)

      val w_respDataMap: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]
      w_respDataMap  += ("data" -> w_respMap)

      if (w_attachmentFileName != null && w_attachmentFile != null)
        addAttachmentInSource(a_enterpriseDAO, a_projectCode, a_itemType, w_itemcode, w_itemId, w_attachmentFileName, w_attachmentFile, a_loginId, a_projectId, w_token)

      JSONUtil.convertMapToJson(w_respDataMap)

    } else {
      null.asInstanceOf[JsonNode]
    }
  }

  override def addAttachmentInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_itemType: String,
    a_itemCode: String, a_itemId : String, a_attachmentFileName : String, a_attachmentFile : String,
    a_loginId: String, a_projectId:String, a_token: String): Unit = {

      try {
        val w_attachmentRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
          IntegrationConstant.SWIFTENTERPRISE_REST_ADD_ATTACHMENT_URL

        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_PUT

        var w_dataMap: Map[String, Any] = Map.empty
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ATTACHMENT_LOGINID -> a_loginId)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE -> IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE_PROJECT)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERCODE -> a_projectCode)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_EFORM_ITEMCODE -> a_itemCode)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ATTACHMENT_FILENAME -> a_attachmentFileName)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ATTACHMENT_BYTE -> a_attachmentFile)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ATTACHMENT_DESCRIPTION -> "")

        val w_dataJSON = JSONUtil.convertMapToJson(w_dataMap)

        m_inboundIntegrationHelper.getRestAPIResponseForSwiftEnterprise(w_attachmentRESTURL, w_contentType, w_methodType, w_dataJSON.toString, a_enterpriseDAO, a_token)
      } catch {
        case e: Exception =>
          WebUtil.logExceptionTrace(e, null)
          val w_JSONNode = JSONUtil.mapStringToJSON(StringUtil.replaceString(e.getMessage, "Failed to send rest request error:",""))
          val w_comment = a_attachmentFileName + "----" + w_JSONNode.at("/message/error").textValue()
          addCommentsInSource(a_enterpriseDAO, a_projectCode, w_comment, a_itemType, a_itemId, a_loginId, a_projectId, a_token)
      }
  }

  override def addCommentsInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_comment: String,
    a_itemType: String, a_itemID: String, a_loginId: String, a_projectId:String, a_token: String): Unit = {

    try {
        val w_fileNameKey: String = "{file_name}"
        val w_commentData = a_comment.split("----")
        val w_fileName = w_commentData(0)
        var w_error : String = w_commentData(1)

        if (w_error.indexOf("file type") != -1) {
            w_error =  I18N.get("KEY_Attachment_Error_FileType_Description", I18N.getDefaultLocale)
            w_error = StringUtil.replaceString(w_error, w_fileNameKey, w_fileName)
            w_error = StringUtil.replaceString(w_error, "{file_extension}", w_fileName.substring(w_fileName.lastIndexOf(".") + 1))
        } else if (w_error.indexOf("size limit") != -1) {
            val w_sizeLimit : String = w_error.substring(w_error.lastIndexOf("size limit"))
            w_error =  I18N.get("KEY_Attachment_Error_FileSize_Description", I18N.getDefaultLocale)
            w_error = StringUtil.replaceString(w_error, w_fileNameKey, w_fileName)
            w_error = StringUtil.replaceString(w_error, "{file_sizelimit}", w_sizeLimit)
        } else {
            w_error =  StringUtil.replaceString(I18N.get("KEY_Attachment_Error_Description",
              I18N.getDefaultLocale), w_fileNameKey, w_fileName) + " "+ w_error
        }

        val w_description: String = w_error
        val w_AddCommentsRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
          IntegrationConstant.SWIFTENTERPRISE_REST_ADD_COMMENTS_URL

        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_PUT

        var w_dataMap: Map[String, Any] = Map.empty
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE -> IntegrationConstant.SWIFTENTERPRISE_OWNERTYPE_PROJECT)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_OWNERID -> a_projectId)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ITEMTYPE -> a_itemType)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_ITEMID -> a_itemID)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_COMMENTS -> w_description)
        w_dataMap += (IntegrationConstant.SWIFTENTERPRISE_LOGGEDBY -> a_loginId)

        val w_dataJSON = JSONUtil.convertMapToJson(w_dataMap)

        m_inboundIntegrationHelper.getRestAPIResponseForSwiftEnterprise(w_AddCommentsRESTURL, w_contentType, w_methodType,
          w_dataJSON.toString, a_enterpriseDAO, a_token)
      } catch {
        case e: Exception =>
          WebUtil.logExceptionTrace(e, null)
      }
  }

  override def sendMail(a_enterpriseDAO:EnterpriseDAO, a_to:String, a_subject:String, a_body:String, a_token: String): Unit = {
    try {
        val w_sendMailRESTURL: String = a_enterpriseDAO.getSource_url + IntegrationConstant.SWIFTENTERPRISE_SEND_MAIL_URL

        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_POST

        var w_dataMap: Map[String, Any] = Map.empty
        w_dataMap += (IntegrationConstant.MAIL_TO -> a_to)
        w_dataMap += (IntegrationConstant.MAIL_SUBJECT -> a_subject)
        w_dataMap += (IntegrationConstant.MAIL_BODY -> a_body)

        val w_dataJSON = JSONUtil.convertMapToJson(w_dataMap)

        m_inboundIntegrationHelper.getRestAPIResponseForSwiftEnterprise(w_sendMailRESTURL, w_contentType, w_methodType, w_dataJSON.toString, a_enterpriseDAO, a_token)
      } catch {
        case e: Exception =>
          WebUtil.logExceptionTrace(e, null)
      }
  }
}
