package com.app.integration.outbound.api.bean

import java.util.Locale

import com.fasterxml.jackson.databind.JsonNode
import com.app.core.crud.enterprise.dataobject.dao.EnterpriseDAO
import com.app.framework.common.constant.GlobalConstant
import com.app.framework.util.general.{DateUtil, JSONUtil, WebUtil}
import com.app.integration.inbound.api.bean.SKSourceInboundIntegrationManagerBean
import com.app.integration.inbound.constant.IntegrationConstant
import com.app.integration.inbound.helper.InboundIntegrationHelper
import com.app.integration.outbound.api.manager.SourceOutboundIntegrationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

import scala.collection.immutable.Map
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

@Component
@Primary
class SKSourceOutboundIntegrationManagerBean extends SourceOutboundIntegrationManager {

  @Autowired
  var m_inboundIntegrationHelper: InboundIntegrationHelper = _

  @Autowired
  var m_inboundIntegrationManagerBean: SKSourceInboundIntegrationManagerBean = _

  override def synchronizeCardDataInSource(a_card: JsonNode, a_enterpriseDAO: EnterpriseDAO,
    a_projectCode: String, a_itemType: String, a_loginId: String, a_projectId:String): JsonNode = {

    val respDataMap: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]

    val w_name = if (a_card.hasNonNull(GlobalConstant.CARDNAME)) a_card.get(GlobalConstant.CARDNAME).asText else null
    val w_description = if (a_card.hasNonNull(GlobalConstant.DESCRIPTION)) a_card.get(GlobalConstant.DESCRIPTION).asText else null
    val w_customFields = if (a_card.hasNonNull("customFields")) a_card.get("customFields") else null
    val w_extItemCode = if (a_card.hasNonNull(GlobalConstant.ITEMCODE)) a_card.get(GlobalConstant.ITEMCODE).asText else null
    val w_cardUniqueId = if (a_card.hasNonNull(GlobalConstant.SOURCECARDID)) a_card.get(GlobalConstant.SOURCECARDID).asText else null
    val w_modify: Boolean = w_extItemCode != null && !"".equals(w_extItemCode)
    val w_IFormSyncRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
      IntegrationConstant.SWIFTKANBAN_REST_IFORM_DETAILS_URL+a_projectId+"/cards?skipMandatory=Y"
    val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
    val w_methodType: String = if (w_modify) IntegrationConstant.METHOD_PUT else  IntegrationConstant.METHOD_POST
    val w_token = m_inboundIntegrationHelper.getTokenDetails(a_enterpriseDAO)
    val w_dataMap: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]
    val w_fieldList = if (a_card.hasNonNull(GlobalConstant.ISSUE_COLLECTOR_FIELD_LIST)) a_card.get(GlobalConstant.ISSUE_COLLECTOR_FIELD_LIST).asText else ""
    val w_fieldLabels = new mutable.StringBuilder()

    var w_attachmentLabel = ""

    val w_attachmentFileName = if (a_card.hasNonNull(GlobalConstant.ATTACHMENT_NAME)) a_card.get(GlobalConstant.ATTACHMENT_NAME).asText else null
    val w_attachmentFile = if (a_card.hasNonNull(GlobalConstant.ATTACHMENT_FILE)) a_card.get(GlobalConstant.ATTACHMENT_FILE).asText else null

    w_fieldLabels.append("Id").append(GlobalConstant.CUSTOM_SEPARATOR)

    if (w_name != null) {
      w_dataMap += (IntegrationConstant.SWIFTKANBAN_IFORM_NAME -> w_name)
      w_fieldLabels.append(IntegrationConstant.SWIFTKANBAN_IFORM_NAME_LABEL).append(GlobalConstant.CUSTOM_SEPARATOR)
    }

    if (w_description != null) {
      w_dataMap += (IntegrationConstant.SWIFTKANBAN_IFORM_DESCRIPTION -> w_description)
      w_fieldLabels.append(IntegrationConstant.SWIFTKANBAN_IFORM_DESCRIPTION_LABEL).append(GlobalConstant.CUSTOM_SEPARATOR)
    }

    w_fieldLabels.append(w_fieldList)

    if (w_attachmentFileName != null && w_attachmentFile != null){
      w_dataMap += ("attachment"-> w_attachmentFileName)
      w_attachmentLabel = "Attachment"
    }

    if (w_customFields != null) {
      w_customFields.forEach(obj => {
        var modVal = obj.get("value").asText
        if(obj.get("dataType").asText.equals("Date")){
          modVal = DateUtil.getStringFromDate(modVal, GlobalConstant.DEFAULT_UI_DATE_FORMAT, GlobalConstant.SWIFTKANBAN_INPUT_DATE_FORMAT, Locale.ENGLISH)
        }
        if(GlobalConstant.ISSUE_COLLECTOR_FIELD_LIST.equals(obj.get("fieldName").asText)){
          w_fieldLabels.append(modVal).append(GlobalConstant.CUSTOM_SEPARATOR).append(w_attachmentLabel)
          modVal = w_fieldLabels.toString()
        }
        w_dataMap += (obj.get("fieldName").asText -> modVal)

      })
    }

    if (a_itemType !=null )
      w_dataMap += (IntegrationConstant.SWIFTKANBAN_IFORM_ITEMTYPE -> a_itemType)

    w_dataMap += (IntegrationConstant.SWIFTKANBAN_IFORM_SOURCE -> IntegrationConstant.SWIFTKANBAN_ISSUE_COLLECTOR_LABEL)
    var w_cardDataArray = ArrayBuffer[Any]()

    if (w_modify && w_extItemCode != null) {
      w_dataMap += (IntegrationConstant.SWIFTKANBAN_IFORM_ITEMCODE -> w_extItemCode)
    }

    w_cardDataArray += w_dataMap
    val w_json: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]
    w_json  += ("card" -> w_cardDataArray)
    val w_dataJSON = JSONUtil.convertMapToJson(w_json)
    val w_IFormResponse = m_inboundIntegrationHelper.getRestAPIResponseForSwiftKanban(w_IFormSyncRESTURL, w_contentType,
      w_methodType, w_dataJSON.toString, a_enterpriseDAO, w_token)

    if (w_IFormResponse != null) {
      val w_IformJSON : JsonNode =  JSONUtil.mapStringToJSON(w_IFormResponse)
      val w_iform : JsonNode = w_IformJSON.at("/Response/details/cardDetails").elements().next().get("card")
      val w_itemcode = if(w_iform.has("ExternalCardId")) w_iform.get("ExternalCardId").textValue() else w_iform.get("id").textValue()
      val w_itemType = w_iform.get("workType").textValue()
      val w_itemId = if(w_iform.has("ExternalCardId")) w_iform.get("id").textValue() else w_cardUniqueId

      val w_respMap: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]

      if (w_itemcode != null)
        w_respMap += ("ItemCode" -> w_itemcode)

      if (w_itemType != null)
        w_respMap += ("itemType" -> w_itemType)

      if (w_itemId != null)
        w_respMap += ("itemId" -> w_itemId)

      respDataMap += ("data" -> w_respMap)

      if (w_attachmentFileName != null && w_attachmentFile != null)
        addAttachmentInSource(a_enterpriseDAO, a_projectId, a_itemType, w_itemcode, w_itemId, w_attachmentFileName,
          w_attachmentFile, a_loginId, a_projectId, w_token)

      JSONUtil.convertMapToJson(respDataMap)
    } else {
      null.asInstanceOf[JsonNode]
    }
  }

  override def addAttachmentInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_itemType: String,
    a_itemCode: String, a_itemId : String, a_attachmentFileName : String, a_attachmentFile : String,
    a_loginId: String, a_projectId:String, a_token: String): Unit = {

    try {
        val w_attachmentRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
          IntegrationConstant.SWIFTKANBAN_REST_ADD_ATTACHMENT_URL + a_projectCode + "/cards/" + a_itemType+ ":" + a_itemCode+"/attachments"
        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_POST
        var w_attachmentArray = ArrayBuffer[Any]()

        var w_dataMap: Map[String, Any] = Map.empty
        w_dataMap += (IntegrationConstant.SWIFTKANBAN_ATTACHMENT_LOGINID -> WebUtil.decryptWithB64(a_enterpriseDAO.getSource_loginid))
        w_dataMap += (IntegrationConstant.SWIFTKANBAN_ATTACHMENT_BYTE -> a_attachmentFile)
        w_dataMap += (IntegrationConstant.SWIFTKANBAN_ATTACHMENT_FILENAME -> a_attachmentFileName)
        w_attachmentArray += w_dataMap

        if (w_attachmentArray.nonEmpty) {
          val w_json: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]
          w_json += ("attachment" -> w_attachmentArray)

          val w_dataJSON = JSONUtil.convertMapToJson(w_json)
          m_inboundIntegrationHelper.getRestAPIResponseForSwiftKanban(w_attachmentRESTURL, w_contentType,
            w_methodType, w_dataJSON.toString, a_enterpriseDAO, a_token)
        }
      } catch {
        case exp: Exception =>
        WebUtil.logExceptionTrace(exp, null)
        var w_toCardErrorText = a_attachmentFileName+ ", file reported via Issue Collector could not be attached to this card"

        try {
          if (exp.getMessage.indexOf("{") > -1) {
            val expStr = exp.getMessage.substring(exp.getMessage.indexOf("{"))
            val w_jsonObj: JsonNode = JSONUtil.mapStringToJSON(expStr)
            w_toCardErrorText += " because " + w_jsonObj.at("/Response/details/attachmentDetails").elements().next().get("responseMsg").elements().next().asText()
            addCommentsInSource(a_enterpriseDAO, a_projectCode, w_toCardErrorText, a_itemType, a_itemId, a_loginId, a_projectId, a_token)
          } else {
            addCommentsInSource(a_enterpriseDAO, a_projectCode, exp.getMessage, a_itemType, a_itemId, a_loginId, a_projectId, a_token)
          }
        } catch {
            case exp: Exception =>
              WebUtil.logExceptionTrace(exp, null)
        }
      }
  }

  override def addCommentsInSource(a_enterpriseDAO: EnterpriseDAO, a_projectCode: String, a_commentText: String,
    a_itemType: String, a_itemID: String, a_loginId: String, a_projectId:String, a_token: String): Unit = {
    var w_toCardCommentArray = ArrayBuffer[Any]()

    try {
      val w_dataMap: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]

      if (a_commentText != null)
        w_dataMap += (IntegrationConstant.SWIFTKANBAN_COMMENT_TEXT -> a_commentText)

      w_dataMap += (IntegrationConstant.SWIFTKANBAN_COMMENTS_BY -> WebUtil.decryptWithB64(a_enterpriseDAO.getSource_loginid))
      w_toCardCommentArray += w_dataMap

      if (w_toCardCommentArray.nonEmpty) {
        val w_json: mutable.HashMap[String, Any] = mutable.HashMap.empty[String, Any]
        w_json += (IntegrationConstant.SWIFTKANBAN_REQ_BODY_COMMENT -> w_toCardCommentArray)
        val w_dataJSON = JSONUtil.convertMapToJson(w_json)

        val w_addCommentRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
          IntegrationConstant.SWIFTKANBAN_REST_COMMENTS_CREATE_URL + a_projectId +
          "/cards/" + a_itemType + WebUtil.substituteEscapeCharacters(IntegrationConstant.SWIFTKANBAN_STORY_MAPPING_REST_CARDCODE_FILTER) +
          a_itemID + "/comments"

        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_POST

        m_inboundIntegrationHelper.getRestAPIResponseForSwiftKanban(w_addCommentRESTURL, w_contentType, w_methodType,
          w_dataJSON.toString, a_enterpriseDAO, a_token)
      }
    } catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
    }
  }

  override def sendMail(a_enterpriseDAO: EnterpriseDAO, a_to: String, a_subject: String, a_body: String, a_token: String): Unit ={
    try {
        val w_sendMailRESTURL: String = m_inboundIntegrationHelper.getRESTBaseUrl(a_enterpriseDAO.source_url) +
          IntegrationConstant.SWIFTKANBAN_SEND_MAIL_URL + "?customattribute=cardURL"

        val w_contentType: String = IntegrationConstant.CONTENTTYPE_APPLICATION_JSON
        val w_methodType: String = IntegrationConstant.METHOD_POST

        var w_dataMap: Map[String, Any] = Map.empty
        w_dataMap += (IntegrationConstant.MAIL_TO -> a_to)
        w_dataMap += (IntegrationConstant.MAIL_SUBJECT -> a_subject)
        w_dataMap += (IntegrationConstant.MAIL_BODY -> a_body)

        val w_dataJSON = JSONUtil.convertMapToJson(w_dataMap)

        m_inboundIntegrationHelper.getRestAPIResponseForSwiftKanban(w_sendMailRESTURL, w_contentType, w_methodType,
          w_dataJSON.toString, a_enterpriseDAO, a_token)
      } catch {
        case e: Exception =>
          WebUtil.logExceptionTrace(e, null)
      }
  }
}