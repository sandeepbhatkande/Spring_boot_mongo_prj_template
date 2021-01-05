package com.app.framework.util.app

import java.util.Locale

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestI18N  extends AssertionsForJUnit{

  @Test
  def testGet(): Unit = {
    val w_expected = "Test message"
    val w_input = "KEY_LABEL_Test_message"
    val w_defaultLocale = new Locale("en", "US")

    val w_actual = I18N.get(w_input, w_defaultLocale)
    assertEquals(w_expected, w_actual)

    //case 2
    val messageArr:Array[String] = Array("KEY_LABEL_Test_message","KEY_TITLE_Project_name","KEY_MESSAGE_Defect_list")
    val w_actaul1 = I18N.get(messageArr, w_defaultLocale)

    val w_expected1:Array[String] = Array("Test message", "Project name", "Defect list")
    assertEquals(w_expected1.toList, w_actaul1.toList)
  }

  @Test
  def testGetString():Unit = {
    val w_expected = "Test message"
    val w_case1 = "KEY_TITLE_Test_message"
    val case1Actual = I18N.getString(w_case1, null)
    assertEquals(w_expected, case1Actual)

    val w_case2 = "KEY_LABEL_Test_message"
    val case2Actual = I18N.getString(w_case2, null)
    assertEquals(w_expected, case2Actual)

    val w_case3 = "KEY_MESSAGE_Test_message"
    val case3Actual = I18N.getString(w_case3, null)
    assertEquals(w_expected, case3Actual)

    val w_case4 = "KEY_ITEMID_Test_message"
    val case4Actual = I18N.getString(w_case4, null)
    assertEquals(w_expected, case4Actual)

    val w_case5 = "KEY_MENU_Test_message"
    val case5Actual = I18N.getString(w_case5, null)
    assertEquals(w_expected, case5Actual)

    val w_case6 = "KEY_RPTINFO_Test_message"
    val case6Actual = I18N.getString(w_case6, null)
    assertEquals(w_expected, case6Actual)

    val w_case7 = "KEY_CHECK_Test_message"
    val case7Actual = I18N.getString(w_case7, null)
    assertEquals(w_expected, case7Actual)

    val w_case8 = "KEY_RADIO_Test_message"
    val case8Actual = I18N.getString(w_case8, null)
    assertEquals(w_expected, case8Actual)

    val w_case9 = "KEY_BUTTON_Test_message"
    val case9Actual = I18N.getString(w_case9, null)
    assertEquals(w_expected, case9Actual)

    val w_case10 = "KEY_LEGEND_Test_message"
    val case10Actual = I18N.getString(w_case10, null)
    assertEquals(w_expected, case10Actual)

    val w_case11 = "KEY_TOOLTIP_Test_message"
    val case11Actual = I18N.getString(w_case11, null)
    assertEquals(w_expected, case11Actual)

    val w_case12 = "KEY_LABEL_Test_message"
    val case12Actual = I18N.getString(w_case12, null)
    assertEquals(w_expected, case12Actual)

    val w_case13 = "KEY_COMBO_Test_message"
    val case13Actual = I18N.getString(w_case13, null)
    assertEquals(w_expected, case13Actual)

    val w_case14 = "KEY_LINK_Test_message"
    val case14Actual = I18N.getString(w_case14, null)
    assertEquals(w_expected, case14Actual)

    val w_case15 = "KEY_Test_message"
    val case15Actual = I18N.getString(w_case15, null)
    assertEquals(w_expected, case15Actual)

    val w_case16 = "LOCK_Test_message"
    val case16Actual = I18N.getString(w_case16, null)
    assertEquals("Resource message not defined", case16Actual)

  }
}
