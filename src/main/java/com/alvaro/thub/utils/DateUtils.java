package com.alvaro.thub.utils;

import java.util.Date;

public class DateUtils {
	public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(year, month - 1, day, hour, minute, second);
		return calendar.getTime();
}
}