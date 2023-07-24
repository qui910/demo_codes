package com.duoduo.demo.jsip.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author zhangzuliang
 * @date 2006-9-20
 */
public class TimeUtil {
	// Default date long
	public static final long DEFAULT_DATE = -5364691200000L;

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final long TIME_OF_A_DAY = 24 * 60 * 60 * 1000;

	/** 获取当前时间戳 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/** 将给定时间戳转换成指定格式字符串 */
	public static String timestamp2String(String format, Date time) {
		if (format == null) {
			format = DATE_PATTERN;
		}
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);

		if (time == null) {
			return null;
		}
		return formatter.format(time);
	}

	/** 将字符串转换成日期信息（格式：yyyy-mm-dd） */
	public static Date string2Date(String dateString) throws ParseException {
		Date date = DateFormat.getDateInstance().parse(dateString);
		return date;
	}

	/** 获取当前时间戳并转换成默认格式（精确到秒）的字符串 */
	public static String getCurrentTime() {
		return getTimeFromTimestamp(getCurrentTimestamp());
	}

	/** 将指定时间戳转换成默认格式（精确到秒）的字符串 */
	public static String getTimeFromTimestamp(Date timestamp) {
		return timestamp2String(DATE_TIME_PATTERN, timestamp);
	}

	/** 获取当前时间戳并转换成默认格式（精确到天）的字符串 */
	public static String getCurrentDate() {
		return getDateFromTimestamp(getCurrentTimestamp());
	}

	/** 将指定时间戳转换成默认格式（精确到天）的字符串 */
	public static String getDateFromTimestamp(Date timestamp) {
		return timestamp2String(DATE_PATTERN, timestamp);
	}

	/** 将指定时间戳转换成默认格式（精确到秒）的字符串 */
	public static String getDateTimeFromTimestamp(Date timestamp) {
		return timestamp2String(DATE_TIME_PATTERN, timestamp);
	}

	/** 判断给定第一个字符串形式时间是否在第二个时间之后 */
	public static boolean after(String time1, String time2) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		return date1.after(date2);
	}

	/** 判断给定第一个字符串形式时间是否在第二个时间之前 */
	public static boolean before(String time1, String time2) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		return date1.before(date2);
	}

	/** 判断给定第一个字符串形式时间是否在第二个时间之前 (精确到秒) */
	public static boolean beforeTime(String time1, String time2) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		return date1.before(date2);
	}

	/**
	 * 判断一个日期是否在另两个日期之间，日期为字符串形式
	 * 
	 * @param midDate
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static boolean between(String midDate, String startDate, String endDate) throws ParseException {
		boolean rt = false;
		if (!(before(midDate, startDate)) && !after(midDate, endDate)) {
			rt = true;
		}
		return rt;
	}

	/** 判断给定第一个字符串形式时间是否与第二个时间相等 */
	public static boolean equals(String time1, String time2) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		return date1.equals(date2);
	}

	/**
	 * @author kongchun
	 * @param time1
	 * @param time2
	 * @return 返回 time1 - time2 的天数差
	 * @throws ParseException
	 */
	public static int compareDate(String time1, String time2) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		long quot;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		int day = new Long(quot).intValue();
		return day;
	}

	/** 将给定字符串形式时间添加指定天数后返回 */
	public static String addDaysToDate(String date, int addDays) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date oldDate = dateFormat.parse(date);
		long t1 = oldDate.getTime();
		return getDateFromTimestamp(new Timestamp(t1 + addDays * TIME_OF_A_DAY));
	}

	/** 将给定字符串形式时间添加指定天数后返回 （精确到秒） */
	public static String addDaysToDateTime(String date, int addDays) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		Date oldDate = dateFormat.parse(date);
		long t1 = oldDate.getTime();
		return getDateTimeFromTimestamp(new Timestamp(t1 + addDays * TIME_OF_A_DAY));
	}

	/** 将给定字符串形式时间添加指定月数后返回 */
	public static String addMonthsToDate(String date, int addMonths) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date oldDate = dateFormat.parse(date);

		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		c.add(Calendar.MONTH, addMonths);
		return fullfillDateTime(DateFormat.getDateInstance().format(c.getTime()));
	}

	/** 将给定字符串形式时间添加指定月数后返回(精确到秒) */
	public static String addMonthsToDateTime(String date, int addMonths) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		Date oldDate = dateFormat.parse(date);

		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		c.add(Calendar.MONTH, addMonths);
		return fullDateTime(DateFormat.getDateTimeInstance().format(c.getTime()));
	}

	/** 将给定字符串形式时间在给定日历中添加指定工期（工作日）后返回 */
	public static String addDaysToDateAccordingGivenCalendar(String date, int addDays, Collection<String> holidays)
			throws ParseException {
		String result = date;

		if (addDays > 0) {
			for (int i = 0; i < addDays; i++) {
				result = getNextWorkday(result, holidays);
			}
		} else if (addDays < 0) {
			for (int i = 0; i > addDays; i--) {
				result = getLastWorkday(result, holidays);
			}
		}

		return result;
	}

	/** 获取下一个工作日 */
	public static String getNextWorkday(String today, Collection<String> holidays) throws ParseException {
		String next = today;
		for (;;) {
			next = addDaysToDate(next, 1);
			if (!isHoliday(next, holidays)) {
				return next;
			}
		}
	}

	/** 获取上一个工作日 */
	public static String getLastWorkday(String today, Collection<String> holidays) throws ParseException {
		String last = today;
		for (;;) {
			last = addDaysToDate(last, -1);
			if (!isHoliday(last, holidays)) {
				return last;
			}
		}
	}

	/** 根据开始时间、工期和日历计算结算结束时间 */
	public static String getEndDate(String startDate, int workDates, Collection<String> holidays) throws ParseException {
		return addDaysToDateAccordingGivenCalendar(startDate, workDates - 1, holidays);
	}

	/** 根据日历计算两个日期间的工作日差距 */
	public static int getDelay(String oldDate, String newDate, Collection<String> holidays) throws ParseException {
		int workDates = getWorkdates(oldDate, newDate, holidays);
		return (workDates > 0) ? workDates - 1 : ((workDates < 0) ? workDates + 1 : 0);
	}

	/** 判断给定日期是否节假日 */
	public static boolean isHoliday(String date, Collection<String> holidays) {
		return holidays.contains(date);
	}

	/** 根据开始时间、结束时间和日历计算工期 */
	public static int getWorkdates(String startDate, String endDate, Collection<String> holidays) throws ParseException {
		if (equals(startDate, endDate)) {
			return 1;
		}

		boolean asc = before(startDate, endDate);
		String temp = startDate;
		int result = 0;
		for (;;) {
			if (asc) {
				result++;
			} else {
				result--;
			}
			temp = asc ? getNextWorkday(temp, holidays) : getLastWorkday(temp, holidays);

			if ((asc && after(temp, endDate)) || (!asc && before(temp, endDate))) {
				return result;
			}
		}
	}

	/** 补全时间，如果2007-6-2补全为2007-06-02 */
	public static String fullfillDateTime(String date) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date oldDate = dateFormat.parse(date);
		long t = oldDate.getTime();
		return getDateFromTimestamp(new Timestamp(t));
	}

	/** 补全时间，如果2007-6-2 00:00:00补全为2007-06-02 00:00:01精确到秒 */
	public static String fullDateTime(String date) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		Date oldDate = dateFormat.parse(date);
		long t = oldDate.getTime();
		return getDateTimeFromTimestamp(new Timestamp(t));
	}

	/** 获取给定日期的周信息（如2007-12-04返回的是2007-12-02 ~ 2007-12-08） */
	public static String getWeekRange(String date) throws ParseException {
		return getWeekRange(string2Date(date));
	}

	/** 获取给定日期的周信息（如2007-12-04返回的是2007-12-02 ~ 2007-12-08） */
	public static String getWeekRange(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		/*
		 * 日期从周一到周日 Date monday = (Date) date.clone();
		 * monday.setTime(monday.getTime() - TIME_OF_A_DAY * (dayOfWeek-1-1));
		 * Date sunday = (Date) date.clone(); sunday.setTime(sunday.getTime() +
		 * TIME_OF_A_DAY * (7-dayOfWeek+1)); return getDateFromTimestamp(monday)
		 * + " ~ " + getDateFromTimestamp(sunday);
		 */
		Date sunday = (Date) date.clone();
		sunday.setTime(sunday.getTime() - TIME_OF_A_DAY * (dayOfWeek - 1));
		Date saturday = (Date) date.clone();
		saturday.setTime(saturday.getTime() + TIME_OF_A_DAY * (7 - dayOfWeek));

		return getDateFromTimestamp(sunday) + " ~ " + getDateFromTimestamp(saturday);

	}

	/** 获取给当前日期的周信息（如2007-12-04返回的是2007-12-02 ~ 2007-12-08） */
	public static String getWeekRange() {
		return getWeekRange(new Date());
	}

	/** 获取给定日期是在当年的第几周 */
	public static int getWeekOfYear(String date) throws ParseException {
		return getWeekOfYear(string2Date(date));
	}

	/** 获取给定日期是在当年的第几周 */
	public static int getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/** 获取当前日期是在当年的第几周 */
	public static int getWeekOfYear() {
		return getWeekOfYear(new Date());
	}

	/** 获取给定日期是在当月的第几天（如2008-03-27返回的是27） */
	public static int getDayOfMonth(String date) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date oldDate;
		oldDate = dateFormat.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		return c.get(Calendar.DAY_OF_MONTH);

	}

	public static String getWeekStart() {
		return getWeekStart(getWeekRange());
	}

	public static String getWeekStart(Date date) {
		return getWeekStart(getWeekRange(date));
	}

	public static String getWeekStart(String weekRange) {
		return weekRange.split("\\s~\\s")[0];
	}

	public static String getWeekEnd() {
		return getWeekEnd(getWeekRange());
	}

	public static String getWeekEnd(Date date) {
		return getWeekEnd(getWeekRange(date));
	}

	public static String getWeekEnd(String weekRange) {
		return weekRange.split("\\s~\\s")[1];
	}

	/**
	 * 将所给日期字符串重新格式化<br>
	 * 如：<br>
	 * <code>TimeUtils.reformatDate("20080204", "yyyyMMdd", "yyyy-MM-dd");</code>
	 * <br>
	 * <code>TimeUtils.reformatDate("2008-02-04", "yyyy-MM-dd", "yyyyMMdd");</code>
	 * <br>
	 */
	public static String reformatDate(String date, String oldFormat, String newFormat) throws ParseException {
		DateFormat format = new SimpleDateFormat(oldFormat);
		Date d = format.parse(date);
		format = new SimpleDateFormat(newFormat);
		return format.format(d);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("------------------------" + addMonthsToDate("2008-04-26", 1));
		// Collection<String> holidays = new ArrayList<String>();
		// String date1 = "2007-11-05";
		// String date2 = "2007-11-09";
		//
		// assert (!after(date1, date2));
		// assert (before(date1, date2));
		// assert (!equals(date1, date2));
		// assert (equals(date1, date1));
		// assert (equals(date2, date2));
		//
		// assert (compareDate(date1, date2) == -4);
		//
		// assert (equals(addDaysToDate(date1, 4), date2));
		// assert (equals(addDaysToDate(date2, -4), date1));
		//
		// assert (equals(addDaysToDateAccordingGivenCalendar(date1, 4,
		// holidays),
		// date2));
		// assert (equals(
		// addDaysToDateAccordingGivenCalendar(date2, -4, holidays), date1));
		//
		// assert (equals(getNextWorkday(date1, holidays), "2007-11-06"));
		// assert (equals(getLastWorkday(date1, holidays), "2007-11-04"));
		// assert (equals(getNextWorkday(date2, holidays), "2007-11-10"));
		// assert (equals(getLastWorkday(date2, holidays), "2007-11-08"));
		//
		// assert (equals(getEndDate(date1, 5, holidays), date2));
		//
		// assert (getDelay(date1, date2, holidays) == 4);
		// assert (getDelay(date2, date1, holidays) == -4);
		//
		// assert (!isHoliday(date1, holidays));
		// assert (!isHoliday(date2, holidays));
		//
		// assert (getWorkdates(date1, date2, holidays) == 5);
		//
		// System.out.println(fullfillDateTime("2007-6-24"));
		//
		// System.out.println(getWeekRange());
		// System.out.println(getWeekRange("2007-12-02"));
		// System.out.println(getWeekRange("2007-12-01"));
		// System.out.println(getWeekRange("2007-12-08"));
		//
		// System.out.println(getWeekOfYear());
		// System.out.println(getWeekOfYear("2007-01-01"));
		// System.out.println(getWeekOfYear("2007-01-06"));
		// System.out.println(getWeekOfYear("2007-01-07"));
		// System.out.println(getWeekOfYear("2007-12-02"));
		// System.out.println(getWeekOfYear("2007-12-01"));
		// System.out.println(getWeekOfYear("2007-12-29"));
		//
		// System.out.println(getWeekStart());
		// System.out.println(getWeekEnd());
		//
		// System.out.println("test success");
	}

}
