package com.app.framework.util.general

import java.time.LocalDate
import java.util.{Date, Locale}

import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.scalatest.junit.AssertionsForJUnit

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[MockitoJUnitRunner])
class JTestDateUtil extends AssertionsForJUnit {
  @Before def initialize(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testGetSysDate(): Unit = {
    assert(DateUtil.getSysDate != null)
  }

  @Test
  def testGetNextThreeMonths(): Unit = {
    assert(DateUtil.getNextThreeMonths.size == 4)
  }

  @Test
  def testGetDate(): Unit = {
    assert(DateUtil.getDate("05-10-1991", "dd-MM-yyyy").isInstanceOf[Date])
  }

  @Test
  def testGetFormatDate(): Unit = {
    assertEquals("05-Oct-1991", DateUtil.getFormatDate(DateUtil.getDate("05-10-1991", "dd-MM-yyyy"), "dd-MMM-yyyy", Locale.ENGLISH))
  }

  @Test
  def testGetThirdMonthLastDate(): Unit = {
    assert(DateUtil.getThirdMonthLastDate.isInstanceOf[Date])
  }

  @Test
  def testGetLastDayOfMonthForGivenMonth(): Unit = {
    assertEquals("31-Jan-2020", DateUtil.getUIStringFromDate(DateUtil.getLastDayOfMonthForGivenMonth("1-2020")))
  }

  @Test
  def testIsDateGreaterThanNumberOfDays(): Unit = {
      assert(DateUtil.isDateGreaterThanNumberOfDays("05-10-1991", 2))
  }

  @Test
  def testIsDateGreaterThanNumberOfDays1(): Unit = {
    assert(DateUtil.isDateGreaterThanNumberOfDays("05-10-1991", 2, "dd-MM-yyyy"))
  }

  @Test
  def testIsDateGreaterThanNumberOfDays2(): Unit = {
    assert(DateUtil.isDateGreaterThanNumberOfDays(DateUtil.getLocalDateFromString("05-10-1991"), 2))
  }

  @Test
  def testGetDateFromSysDate(): Unit = {
    assert(DateUtil.getDateFromSysDate(2).isInstanceOf[LocalDate])
  }

  @Test
  def testGetFirstDayOfWeek(): Unit = {
      assert(DateUtil.getFirstDayOfWeek(2).isInstanceOf[Date])
  }

  @Test
  def testGetLocalDateDifference(): Unit = {
    assertEquals(5, DateUtil.getLocalDateDifference(DateUtil.getLocalDateFromString("05-10-1991"), DateUtil.getLocalDateFromString("10-10-1991")))
  }

  @Test
  def testGetStringFromLocalDate(): Unit = {
    assertEquals("1991-10-05", DateUtil.getStringFromLocalDate(DateUtil.getLocalDateFromString("05-10-1991")))
  }

  @Test
  def testGetLocalDateFromString(): Unit = {
    assert(DateUtil.getLocalDateFromString("05-10-1991").isInstanceOf[LocalDate])
  }

  @Test
  def testGetLocalDateFromString1(): Unit = {
    assert(DateUtil.getLocalDateFromString("05-10-1991", "dd-MM-yyyy").isInstanceOf[LocalDate])
  }

  @Test
  def testGetMinDate(): Unit = {
    val w_arr: ArrayBuffer[LocalDate] = new ArrayBuffer[LocalDate]()
    w_arr += DateUtil.getLocalDateFromString("05-10-1991")
    w_arr += DateUtil.getLocalDateFromString("21-01-2020")

    assertEquals(DateUtil.getLocalDateFromString("05-10-1991"), DateUtil.getMinDate(w_arr))
  }

  @Test
  def testGetUIStringFromDate(): Unit = {
    assertEquals("05-Oct-1991", DateUtil.getUIStringFromDate(DateUtil.getDate("05-10-1991", "dd-MM-yyyy")))
  }

  @Test
  def testGetUIStringFromDate1(): Unit = {
    assertEquals("1991-Oct-05", DateUtil.getUIStringFromDate(DateUtil.getDate("05-10-1991", "dd-MM-yyyy"), "yyyy-MMM-dd"))
  }

  @Test
  def testGetUIStrngFromLocalDate(): Unit = {
    assertEquals("05-Oct-1991", DateUtil.getUIStrngFromLocalDate(DateUtil.getLocalDateFromString("05-10-1991")))
  }

  @Test
  def testGetDateFromString(): Unit = {
    assert(DateUtil.getDateFromString("05-10-1991","dd-MM-yyyy").isInstanceOf[Date])
  }

  /*@Test
  def testGetMonthName(): Unit = {
    assertEquals("July", DateUtil.getMonthName("05-10-1991"))
  }*/

  @Test
  def testGetStringFromDate(): Unit = {
    assertEquals("1991-Oct-05", DateUtil.getStringFromDate("05-10-1991", "dd-MM-yyyy", "yyyy-MMM-dd", Locale.ENGLISH))
  }

  /*@Test
  def testGetDateFromDBDate(): Unit = {
    assertEquals(DateUtil.getDateFromString("05-10-1991","dd-MM-yyyy"), DateUtil.getDateFromDBDate("686601000000"))
  }*/

  /*@Test
  def testChangeDataTimeZone(): Unit = {
    assertEquals("04-10-1991", DateUtil.changeDataTimeZone(DateUtil.getDateFromString("05-10-1991","dd-MM-yyyy"), "UTC", "dd-MM-yyyy"))
  }*/
}