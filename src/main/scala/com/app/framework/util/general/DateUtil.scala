package com.app.framework.util.general

import java.text.{ParseException, SimpleDateFormat}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util._

import com.app.framework.common.constant.GlobalConstant
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object DateUtil {
	
	def getSysDate: Date = {
		new Date(System.currentTimeMillis())
	}
	
	def getNextThreeMonths: mutable.HashMap[String, String] = {
		
		val w_cal = Calendar.getInstance()
		val w_dateFormat = new SimpleDateFormat("MMM-YYYY")
		val w_monthMap = new mutable.HashMap[String, String]()
		
		w_monthMap += ("-1" -> "All Months")
		
		w_cal.add(Calendar.MONTH, -7)
		
		getMonthYearMap(w_cal, w_dateFormat, w_monthMap)
		w_cal.add(Calendar.MONTH, 1)
		getMonthYearMap(w_cal, w_dateFormat, w_monthMap)
		w_cal.add(Calendar.MONTH, 1)
		getMonthYearMap(w_cal, w_dateFormat, w_monthMap)
		
		w_monthMap
	}
	
	def getDate(a_date: String, a_dateFormat: String): Date = {
		var w_date: Date = null

		try {
			val w_format = new java.text.SimpleDateFormat(a_dateFormat)
			w_date = w_format.parse(a_date)
		}
		catch {
			case e: ParseException =>
				WebUtil.logExceptionTrace(e, null)
		}

		w_date
	}

	def getDate(a_date: String) : Date = {
		var w_date: Date = null

		try {
			w_date = ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.UTC).parseDateTime(a_date).toDate
		}
		catch {
			case e: ParseException =>
				WebUtil.logExceptionTrace(e, null)
		}

		w_date
	}
	
	def getFormatDate(a_date: Date, a_dateFormat: String, locale: Locale): String = {
		
		val w_format = new java.text.SimpleDateFormat(a_dateFormat, locale)
		w_format.format(a_date)
	}
	
	def getThirdMonthLastDate: Date = {
		
		val w_cal = Calendar.getInstance()
		w_cal.add(Calendar.MONTH, 4)
		
		w_cal.set(Calendar.DAY_OF_MONTH, 1)
		
		val sdf = new SimpleDateFormat(GlobalConstant.DEFAULT_DATE_FORMAT)
		sdf.parse(sdf.format(w_cal.getTime))
	}
	
	def getLastDayOfMonthForGivenMonth(a_month: String): Date = {
		
		val  w_month = a_month.split("-")(0).toInt
		val w_year = a_month.split("-")(1).toInt
		
		val w_cal = Calendar.getInstance()
		w_cal.set(Calendar.MONTH, w_month)
		w_cal.set(Calendar.DAY_OF_MONTH, 1)
		w_cal.set(Calendar.YEAR, w_year)
		w_cal.add(Calendar.DATE, -1)
		
		val sdf = new SimpleDateFormat(GlobalConstant.DEFAULT_DATE_FORMAT)
		sdf.parse(sdf.format(w_cal.getTime))
	}
	
	def isDateGreaterThanNumberOfDays(a_dateStr: String, a_numberOfDays: Int): Boolean = {
		
		val w_currentDate = LocalDate.now()
		val w_expectedDate = getLocalDateFromString(a_dateStr)
		
		ChronoUnit.DAYS.between(w_expectedDate, w_currentDate) > a_numberOfDays
	}
	
	def isDateGreaterThanNumberOfDays(a_dateStr: String, a_numberOfDays: Int, a_dateFormat: String): Boolean = {
		
		val w_currentDate = LocalDate.now()
		val w_expectedDate = getLocalDateFromString(a_dateStr, a_dateFormat)
		
		ChronoUnit.DAYS.between(w_expectedDate, w_currentDate) > a_numberOfDays
	}

	def isDateGreaterThanNumberOfDays(a_date: LocalDate, a_numberOfDays: Int): Boolean = {

		val w_currentDate = LocalDate.now()

		ChronoUnit.DAYS.between(a_date, w_currentDate) > a_numberOfDays
	}
	
	def getDateFromSysDate(a_noOfDays: Long): LocalDate = {
		
		LocalDate.now().minusDays(a_noOfDays)
	}

	
	def getFirstDayOfWeek(a_noOfDays: Int): Date = {
		
		val w_calendar = Calendar.getInstance()
		w_calendar.set(Calendar.HOUR_OF_DAY, 0)
		w_calendar.clear(Calendar.MINUTE)
		w_calendar.clear(Calendar.SECOND)
		w_calendar.clear(Calendar.MILLISECOND)
		
		// get start of this week in milliseconds
		w_calendar.set(Calendar.DAY_OF_WEEK, w_calendar.getFirstDayOfWeek)
		
		if (a_noOfDays != 0)
			w_calendar.add(Calendar.DAY_OF_MONTH, a_noOfDays * -1)
		
		val sdf = new SimpleDateFormat(GlobalConstant.DEFAULT_DATE_FORMAT)
		sdf.parse(sdf.format(w_calendar.getTime))
	}

	def getLocalDateDifference(a_startDate: LocalDate, a_endDate: LocalDate): Long = {
		
		ChronoUnit.DAYS.between(a_startDate, a_endDate)
	}
	
	def getStringFromLocalDate(a_date: LocalDate): String = {
		
		val w_formatter = DateTimeFormatter.ofPattern(GlobalConstant.DEFAULT_DATE_FORMAT)
		a_date.format(w_formatter)
	}
	
	def getLocalDateFromString(a_dateStr: String): LocalDate = {
		val formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][yyyy-MM-dd][MM-dd-yyyy][dd-MMM-yyyy]")
		LocalDate.parse(a_dateStr, formatter)
	}
	
	def getLocalDateFromString(a_dateStr: String,
		a_dateFormat: String = "[dd-MM-yyyy][yyyy-MM-dd][MM-dd-yyyy][dd-MMM-yyyy]"): LocalDate = {
		val formatter = DateTimeFormatter.ofPattern(a_dateFormat)
		
		LocalDate.parse(a_dateStr, formatter)
	}
	
	private def getMonthYearMap(a_cal: Calendar, a_dateFormat: SimpleDateFormat,
		a_monthMap: mutable.HashMap[String, String]) = {

		a_monthMap += (String.valueOf(a_cal.get(Calendar.MONTH) + 1) -> a_dateFormat.format(a_cal.getTime))
	}
	
	def getMinDate(a_dateList: ArrayBuffer[LocalDate]): LocalDate = {
		
		val w_dateList: java.util.Collection[LocalDate] = a_dateList.asJavaCollection
		Collections.min(w_dateList)
	}
	
	def getUIStringFromDate(a_date: Date): String = {
		
		new SimpleDateFormat(GlobalConstant.DEFAULT_UI_DATE_FORMAT).format(a_date)
	}

	def getUIStringFromDate(a_date: Date, a_dateFormate: String): String = {

		new SimpleDateFormat(a_dateFormate).format(a_date)
	}
	
	def getUIStrngFromLocalDate(a_date: LocalDate): String = {
		
		val formatter = DateTimeFormatter.ofPattern(GlobalConstant.DEFAULT_UI_DATE_FORMAT)
		a_date.format(formatter)
	}
	
	def getDateFromString(a_str: String, a_dateFormat: String): Date = {
		
		new SimpleDateFormat(a_dateFormat).parse(a_str)
	}
	
	def getMonthName(a_date: String): String = {
		
		val w_month = a_date.split("-")(0).toInt
		
		val w_cal = Calendar.getInstance()
		w_cal.set(Calendar.MONTH, w_month)
		
		w_cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault)
		
	}
	
	def getStringFromDate(a_str: String, a_currentDateFormat: String, a_reqDateFormat: String, locale: Locale): String = {
		
		getFormatDate(new SimpleDateFormat(a_currentDateFormat).parse(a_str), a_reqDateFormat, locale)
	}

	def getDateFromDBDate( a_date: String ): Date = {
		new Date( a_date.toLong )
	}

	def changeDataTimeZone(a_date: Date, a_timeZone: String, a_dateFormate: String): String = {
		val w_formatter = new SimpleDateFormat( if ( a_dateFormate == null) "dd-MMM-yyyy HH:mm:ss" else a_dateFormate )

		if ( a_timeZone != null ) {
			val timezone = TimeZone.getTimeZone(a_timeZone)
			w_formatter.setTimeZone( timezone )
		}

		w_formatter.format(a_date)
	}
}