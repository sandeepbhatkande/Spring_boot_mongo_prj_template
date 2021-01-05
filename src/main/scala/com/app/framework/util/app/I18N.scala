package com.app.framework.util.app

import java.util
import java.util.{Locale, ResourceBundle}

import com.app.framework.common.constant.GlobalConstant
import com.app.framework.exception.AppAppException
import com.app.framework.util.general.{StringUtil, WebUtil}

import scala.collection.mutable.ArrayBuffer

object I18N {
  var ALLOWED_LOCALES = new util.HashSet[String]
  val m_ResBundleMap = new util.HashMap[Locale, ResourceBundle]

  private var m_defaultLocale:Locale = _

  ALLOWED_LOCALES.add("en_US")
  ALLOWED_LOCALES.add("de_DE")
  ALLOWED_LOCALES.add("fr_FR")
  ALLOWED_LOCALES.add("zh_CN")
  ALLOWED_LOCALES.add("pt_BR")

  var w_isLocaleAvailable = true

  if (!ALLOWED_LOCALES.contains(Locale.getDefault().toString)) {
      w_isLocaleAvailable = false
  }

  if (w_isLocaleAvailable) {
      m_defaultLocale = Locale.getDefault()
  } else {
      m_defaultLocale = new Locale("en", "US")
  }

  def init (a_userLocale:Locale) : Unit = {
    var w_ResourceBundle: ResourceBundle = null

    if (a_userLocale != null) {
      w_ResourceBundle = loadPropertiesFromResourceBundle ("I18N", a_userLocale)
    } else {
      w_ResourceBundle = loadPropertiesFromResourceBundle ("I18N", Locale.getDefault)
    }

    m_ResBundleMap.put (a_userLocale, w_ResourceBundle)
  }


  def loadPropertiesFromResourceBundle(w_fileName: String, a_userLocale: Locale): ResourceBundle = {
    ResourceBundle.getBundle("I18N."+ w_fileName, a_userLocale)
  }

  def get(a_Key: String, a_userLocale: Locale): String = {
    var w_labelStr = ""

    try {
      var w_userLocale = a_userLocale

      if (a_userLocale == null || !ALLOWED_LOCALES.contains(a_userLocale.toString))
        w_userLocale = getDefaultLocale

      if (!m_ResBundleMap.containsKey(w_userLocale))
        init(w_userLocale)

      val w_resourceBundle = m_ResBundleMap.get(a_userLocale)
      w_labelStr = getString(a_Key, w_resourceBundle)

      if (w_labelStr == null)
        w_labelStr = "RESOURCE_MESSAGE_NOT_DEFINED"

    } catch {
      case e: Exception =>
        WebUtil.logExceptionTrace(e, null)
        var m_errorList: ArrayBuffer[String] = new ArrayBuffer[String]()
        m_errorList += e.getMessage
        throw new AppAppException("Error", e.getMessage, m_errorList)
    }

    w_labelStr
  }

  def get(w_keyList: Array[String], a_userLocale: Locale): Array[String] = {
    val w_valueList = new Array[String](w_keyList.length)
    var i = 0

    while ( { i < w_keyList.length })
    {
      w_valueList(i) = get(w_keyList(i), a_userLocale)

      {
        i += 1
      }
    }

    w_valueList
  }

  def getDefaultLocale: Locale = m_defaultLocale

  def getString(a_Key: String, a_ResourceBundle: ResourceBundle): String = { // throws Exception
    var w_str: String = ""

    if (a_Key != null && a_ResourceBundle != null && a_ResourceBundle.containsKey(a_Key))
      w_str = a_ResourceBundle.getString(a_Key.trim)
    else {
      w_str = a_Key

      if (w_str != null && w_str.startsWith("KEY_")) {
        if (w_str.startsWith("KEY_TITLE_"))
          w_str = w_str.substring("KEY_TITLE_".length)
        else if (w_str.startsWith("KEY_LABEL_MASTER_"))
          w_str = w_str.substring("KEY_LABEL_MASTER_".length)
        else if (w_str.startsWith("KEY_MESSAGE_"))
          w_str = w_str.substring("KEY_MESSAGE_".length)
        else if (w_str.startsWith("KEY_ITEMID_"))
          w_str = w_str.substring("KEY_ITEMID_".length)
        else if (w_str.startsWith("KEY_MENU_"))
          w_str = w_str.substring("KEY_MENU_".length)
        else if (w_str.startsWith("KEY_RPTINFO_"))
          w_str = w_str.substring("KEY_RPTINFO_".length)
        else if (w_str.startsWith("KEY_CHECK_"))
          w_str = w_str.substring("KEY_CHECK_".length)
        else if (w_str.startsWith("KEY_RADIO_"))
          w_str = w_str.substring("KEY_RADIO_".length)
        else if (w_str.startsWith("KEY_BUTTON_"))
          w_str = w_str.substring("KEY_BUTTON_".length)
        else if (w_str.startsWith("KEY_LEGEND_"))
          w_str = w_str.substring("KEY_LEGEND_".length)
        else if (w_str.startsWith("KEY_TOOLTIP_"))
          w_str = w_str.substring("KEY_TOOLTIP_".length)
        else if (w_str.startsWith("KEY_LABEL_"))
          w_str = w_str.substring("KEY_LABEL_".length)
        else if (w_str.startsWith("KEY_COMBO_"))
          w_str = w_str.substring("KEY_COMBO_".length)
        else if (w_str.startsWith("KEY_LINK_"))
          w_str = w_str.substring("KEY_LINK_".length)
        else if (w_str.startsWith("KEY_"))
          w_str = w_str.substring("KEY_".length)

        w_str = StringUtil.replaceString(w_str, "_", " ")
      }
      else if (w_str != null && w_str.startsWith("LOCK_")) w_str = GlobalConstant.RESOURCE_MESSAGE_NOT_DEFINED
    }

    w_str
  }
}
