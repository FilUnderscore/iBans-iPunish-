package me.wedevsm8.ipunish.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilTime 
{
	public static String getTimeWhen(long time)
	{
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Long.valueOf(time));
	}
	
	public static String getCurrentTime()
	{
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public static enum TimeUnit
	{
		FIT,
		DAYS,
		HOURS,
		MINUTES,
		SECONDS,
		MILLISECONDS;
	}
	
	public static double convert(long time, int trim, TimeUnit type)
	{
		if(type == TimeUnit.FIT)
		{
			if(time < 60000L)
				type = TimeUnit.SECONDS;
			else if(time < 360000L)
				type = TimeUnit.MINUTES;
			else if(time < 86400000L)
				type = TimeUnit.HOURS;
			else
				type = TimeUnit.DAYS;
		}
		
		if(type == TimeUnit.DAYS)
			return trim(trim, time / 86400000.0D);
		else if(type == TimeUnit.HOURS)
			return trim(trim, time / 3600000.0D);
		else if(type == TimeUnit.MINUTES)
			return trim(trim, time / 60000.0D);
		else if(type == TimeUnit.SECONDS)
			return trim(trim, time / 1000.0D);
		
		return trim(trim, time);
	}
	
	public static String convertString(long time)
	{
		return convertString(time, 1, TimeUnit.FIT);
	}
	
	public static String convertString(long time, int trim)
	{
		return convertString(time, trim, TimeUnit.FIT);
	}
	
	public static String convertString(long time, int trim, TimeUnit type)
	{
		if(time == -1L)
		{
			return "Permanent";
		}
		
		if(type == TimeUnit.FIT)
		{
			if(time < 60000L)
				type = TimeUnit.SECONDS;
			else if(time < 3600000L)
				type = TimeUnit.MINUTES;
			else if(time < 86400000L)
				type = TimeUnit.HOURS;
			else
				type = TimeUnit.DAYS;
		}
		
		if(type == TimeUnit.DAYS)
			return trim(trim, time / 86400000.0D) + " Days";
		else if(type == TimeUnit.HOURS)
			return trim(trim, time / 3600000.0D) + " Hours";
		else if(type == TimeUnit.MINUTES)
			return trim(trim, time / 600000.0D) + " Minutes";
		else if(type == TimeUnit.SECONDS)
			return trim(trim, time / 1000.0D) + " Seconds";
		
		return trim(trim, time) + " Milliseconds";
	}
	
	public static long convertLong(int time, TimeUnit type)
	{
		if(time == -1)
		{
			return -1L;
		}
		
		if(type == TimeUnit.DAYS)
			return Long.valueOf(time * 86400000L);
		else if(type == TimeUnit.HOURS)
			return Long.valueOf(time * 3600000L);
		else if(type == TimeUnit.MINUTES)
			return Long.valueOf(time * 600000L);
		else if(type == TimeUnit.SECONDS)
			return Long.valueOf(time * 1000L);
		
		return Long.valueOf(time);
	}
	
	public static boolean elapsed(long from, long required)
	{
	    return System.currentTimeMillis() - from > required;
	}
	
	public static long getRemaining(long from)
	{
		return System.currentTimeMillis() - from;
	}
	
	public static double trim(int amount, double d)
	{
		String format = "#.#";
		
		for(int i = 1; i < amount; i++)
		{
			format = format + "#";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat(format);
		
		return Double.valueOf(decimalFormat.format(d)).doubleValue();
	}
}