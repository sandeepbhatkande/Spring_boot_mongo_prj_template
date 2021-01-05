package com.app.framework.common.constant

import java.util.Date

object GlobalConstant {
  val LOGINID : String = "loginID"
  val DEFAULT_UI_DATE_FORMAT = "dd-MMM-yyyy"
  val DEFAULT_UI_DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss"
  val DEFAULT_DATE_NULL_VALUE = new Date()

  //Date Format
  val DEFAULT_DATE_FORMAT: String = "yyyy-MM-dd"
  val LOGGED_IN_USER = "loggedInUser"
  val JWT_TOKEN = "jwtToken"
  val STATUS_OPEN: String = "Open"
  val STATUS_CLOSED: String = "Closed"
  val STATUS: String = "status"
  val DEFAULT_USER_SECRET = "111111"
  val UTF8_CHARACTER_ENCODING = "UTF-8"
  val SWIFTENTERPRISE_INTEGRATION_DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss"
  val SWIFTKANBAN_INTEGRATION_DATE_FORMAT = "yyyy-mm-dd HH:mm:ss"
  val SWIFTENTERPRISE_LONG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S"
  val SWIFTKANBAN_INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
  val MAIL_INPUT_DATE_FORMAT = "yyyy-MM-dd"
  val YES = "Y"
  val NO = "N"
  val ACTIVE = "Active"
  val INACTIVE = "Inactive"
  val TIME_ZONE = "userTimeZone"
  val HASH_SEPERATOR = "#"
  val DEFAULT_LOOP_INITIALIZER = 0
  val LOOP_INITIAL_VALUE_1 = 1
  val ITEMTYPE_RELEASE = "RELEASE"
  val ITEMTYPE_SPRINT = "SPRINT"

  //Card Related
  val CARDTYPE= "cardType"
  val PROJECTID = "projectid"
  val USERPROFILEURL= "userProfileUrl"
  val CARDNAME= "cardName"
  val LABEL= "label"
  val CLASSNAME= "className"
  val POSITION= "pos"
  val ROW = "row"
  val ID = "id"
  val DESCRIPTION= "description"
  val DESCFIELDTYPE= "descFieldType"
  val NAME = "name"
  val DEFAULT_QUEUE_WBS_PAD = 5
  val CREATEDBY = "createdBy"
  val ENTERPRISEID = "enterpriseId"
  val ITEMCODE = "itemCode"
  val ITEMTYPE = "itemType"
  val PROJECTCODE ="projectCode"

  val COMMA = ","
  val NULL_VALUE = "null"
  val SOURCECARDID = "sourceItemId"
  val SOURCE_ITEMID ="srcItemId"
  val CUSTOM_SEPARATOR = "^*!^"

  val SORT_ORDER_DESC = "SORT_ORDER_DESC"
  val SORT_ORDER_ASC = "SORT_ORDER_ASC"

  val JSON_KEY_MISSING_NULL_VALUES = "Following key is missing in json/ Key is having null values  : - "

  val ACTION_LEVEL_ALL = "-1"
  val RESOURCE_MESSAGE_NOT_DEFINED= "Resource message not defined"

  val SOURCE_EXTERNAL_PROJECT_ID = "sourceExternalProjectId"
  val SOURCE_PROJECT_ID = "sourceProjectId"

  val ATTACHMENT_NAME = "attachmentName"
  val ATTACHMENT_FILE = "attachmentFile"

  val RETURN_TYPE_APPLICATION_JSON = "application/json"
  val RETURN_TYPE_APPLICATION_XML = "application/xml"
  val REST_INPUT_INTEGER = "integer"
  val REST_INPUT_STRING = "string"
  val REST_INPUT_OBJECT = "object"
  val SWAGGER_METHOD_GET = "get"
  val SWAGGER_METHOD_POST = "post"

  val ISSUE_COLLECTOR_FIELD_LIST = "ISSUE_COLLECTOR_FIELD_LIST"
  val APPLICATION_SUPPORT = "$applicationSupport"
  val HASRTFCONTENT = "HASRTFCONTENT"
}