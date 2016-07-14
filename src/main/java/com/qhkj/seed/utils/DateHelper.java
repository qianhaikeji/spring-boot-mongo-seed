package com.qhkj.seed.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qhkj.seed.exceptions.ServiceException;

public class DateHelper extends Date {
	public static final String FORMAT_DATETIME = "yyyy-MM-dd-HH:mm:ss";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATE_NUM = "yyyyMMdd";
	public static final String FORMAT_DATETIME_NUM = "yyyyMMddHHmmss";
	public static final String FORMAT_MONTH = "yyyy-MM";
	public static final String FORMAT_YAER = "yyyy";
	
	public static boolean isAfterDate(Date date, Date target) throws ServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		String nowStr = sdf.format(date);
		String lastStr = sdf.format(target);

		try {
			Date nowDay = sdf.parse(nowStr);
			Date lastDay = sdf.parse(lastStr);
			return nowDay.after(lastDay);
		} catch (ParseException e) {
			throw new ServiceException("无效的日期!");
		}
	}
	
	public static Date toDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		String str = sdf.format(date);
		
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			throw new ServiceException("无效的日期!");
		}
	}
	
	public static Date toDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new ServiceException("无效的日期!");
		}
	}
}
