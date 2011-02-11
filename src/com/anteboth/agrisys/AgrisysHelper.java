package com.anteboth.agrisys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.widget.DatePicker;

public class AgrisysHelper {
	
	
	/**
	 * Formats the input date to string.
	 * @param d the date
	 * @return the string representation of the date
	 */
	public static String formatDate(Date d) {
		return d != null ? DateFormat.getDateInstance().format(d) : "";
	}
	
	/**
	 * Parses the date string and returns the date value.
	 * Input format is: <b>dd.MM.yyyy HH:mm:ss</b>.
	 * 
	 * @param s the input string
	 * @return the parsed date value, null if input is null or invalid
	 */
	public static Date toDate(String s) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Convinient method to update a date in a {@link DatePicker}.
	 * @param picker the date picker
	 * @param date the date value
	 */
	public static void updateDate(DatePicker picker, Date date) {
		if (picker != null && date != null) {
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(date);
			
	        int y = c.get(Calendar.YEAR);
	        int m = c.get(Calendar.MONTH);
	        int d = c.get(Calendar.DAY_OF_MONTH);
	        
	        picker.updateDate(y, m, d);
		}
	}

}
