package com.std4453.freemclauncher.util;

import java.text.DateFormat;
import java.text.ParseException;

public class TimeHelper {
	public static long getTime(String str) {
		try {
			return DateFormat.getDateInstance().parse(str).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
