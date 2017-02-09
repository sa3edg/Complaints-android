package com.ebttikarat.complaints.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static String[] getTripTimeFromMilliseconds(String time)
	{
		if(time == null  || "".equals(time))
		{
			return null;
		}
		String[] formatedTime = new String[2];
		Date date = new Date(Long.parseLong(time));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
//		String amPm =  calendar.get(Calendar.HOUR_OF_DAY) <= 12 ? "AM" : "PM";
//		formatedTime[0] = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + amPm;
		DateFormat format = new SimpleDateFormat("EEE, d MMM,hh:mm a");
		String formatedDateStr = format.format(calendar.getTime());
		formatedTime[0] = formatedDateStr.substring(0, formatedDateStr.lastIndexOf(",") + 1);
		formatedTime[1] = formatedDateStr.substring(formatedDateStr.lastIndexOf(",") + 1, formatedDateStr.length());
		return formatedTime;
	}
	
	public static String getTripTimeFormated(String time)
	{
		if(time == null  || "".equals(time))
		{
			return null;
		}
		Date date = new Date(Long.parseLong(time));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		DateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		return format.format(calendar.getTime());
	}
	public static String getLastUpdateDate()
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		return format.format(calendar.getTime());
	}
	

}
