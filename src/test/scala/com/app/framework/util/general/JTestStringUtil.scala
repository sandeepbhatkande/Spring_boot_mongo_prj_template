package com.app.framework.util.general

import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[MockitoJUnitRunner])
class JTestStringUtil extends AssertionsForJUnit {

  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testIsValidString(): Unit = {
    assertEquals(false, StringUtil.isValidString(""))
  }

  @Test
  def testGetFormattedString(): Unit = {
    val w_dateFieldList: List[Any] = List("date")
    val w_attributeValue = "AttrValue"

    assertEquals(w_attributeValue, StringUtil.getFormattedString(null, "duemonth", w_attributeValue))
    assertEquals("05-Oct-1991", StringUtil.getFormattedString(w_dateFieldList, "DATE", "05-10-1991"))
    assertEquals("344.57", StringUtil.getFormattedString(w_dateFieldList, "DATE1", "344.567"))
  }

  @Test
  def testGetNDecimalRoundOff(): Unit = {
    assert(StringUtil.getNDecimalRoundOff(24.467, 2) == 24.47)
  }

  @Test
  def testGetWbsCode(): Unit = {
    assertEquals("0001.00001", StringUtil.getWbsCode("0001", 1))
  }

  @Test
  def testPadString(): Unit = {
    assertEquals("10000", StringUtil.padString("1", 5, "0", a_rightPad = true))
    assertEquals("00001", StringUtil.padString("1", 5, "0", a_rightPad = false))
  }

  @Test
  def testReplaceString(): Unit = {
    assertEquals("", StringUtil.replaceString(null, null, null))
    assertEquals("original", StringUtil.replaceString("original", null, null))
    assertEquals("origilan", StringUtil.replaceString("original", "nal", "lan"))
  }

  @Test
  def testReplaceSubstring(): Unit = {
    assertEquals(new StringBuilder, StringUtil.replaceSubstring(null, null, null))
    assertEquals("origilan", StringUtil.replaceSubstring(new StringBuilder("original"), "nal", "lan").toString)
  }
}